package com.team2.master.config;

import com.team2.master.command.domain.entity.Item;
import com.team2.master.command.domain.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 운영 DB item_spec 자유 텍스트가 일관 포맷이 아니어서 PDF/문서 표 출력 품질 저하.
 * 물리 치수가 의미있는 품목(솔라모듈/유리 등)에 대해 W/D/H 를 canonical 값으로 강제 설정.
 *
 * itemSpec 은 ItemCommandService.assembleSpec 정책과 동일하게 W/D/H 셋 다 채워지면
 * "1722 × 1134 × 35 mm" 형식으로 자동 조립 (Item 엔티티 onUpdate hook 외부에서 직접 set).
 *
 * 정책:
 * - 매핑된 item_code 의 W/D/H 를 canonical 값으로 강제 설정 (idempotent)
 * - itemSpec 은 W/D/H 로부터 derived — 별도 자유 텍스트 입력 없음
 * - 매핑되지 않은 item_code 는 무시 (cable, sealant 등 W×D×H 가 의미 없는 품목)
 * - 사용자가 admin 화면에서 수정한 값도 매번 재시작마다 canonical 로 되돌아감 — 운영팀이
 *   admin 화면에서 자유롭게 편집하려면 매핑 제거 또는 ENV flag 도입 필요 (현재 시드 정합성 우선)
 */
@Component
public class ItemSpecBackfillBootstrap {

    private static final Logger log = LoggerFactory.getLogger(ItemSpecBackfillBootstrap.class);

    /** 물리 치수가 의미있는 품목의 canonical W/D/H. INT 컬럼이라 mm 단위 정수. */
    private static final Map<String, ItemDimensions> CANONICAL = new LinkedHashMap<>();
    static {
        // 솔라모듈 — 시중 표준 두께 35mm 가정
        CANONICAL.put("ITM010", new ItemDimensions(1722, 1134, 35));
        CANONICAL.put("ITM011", new ItemDimensions(2094, 1134, 35));
        CANONICAL.put("ITM012", new ItemDimensions(2384, 1303, 35));
        // 강화유리 — 두께 3.2mm 는 INT 이라 3mm 로 절사
        CANONICAL.put("ITM006", new ItemDimensions(1722, 1134, 3));
    }

    /** ItemCommandService.assembleSpec 와 동일한 포맷으로 W/D/H 를 itemSpec 문자열로 조립. */
    private static String assembleSpec(int w, int d, int h) {
        return w + " × " + d + " × " + h + " mm";
    }

    private final ItemRepository itemRepository;

    public ItemSpecBackfillBootstrap(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void normalizeOnReady() {
        // 1) 캐노니컬 매핑된 품목의 W/D/H 강제 설정
        int canonicalApplied = 0;
        for (Map.Entry<String, ItemDimensions> entry : CANONICAL.entrySet()) {
            Item item = itemRepository.findByItemCode(entry.getKey()).orElse(null);
            if (item == null) continue;
            ItemDimensions d = entry.getValue();
            if (matchesCanonical(item, d)) continue;
            String derivedSpec = assembleSpec(d.width(), d.depth(), d.height());
            item.updateInfo(
                    null, null, derivedSpec,
                    d.width(), d.depth(), d.height(),
                    null, null, null, null, null, null
            );
            itemRepository.save(item);
            canonicalApplied++;
        }

        // 2) 전체 품목 itemSpec 정합성: W/D/H 셋 다 있으면 derived 로, 아니면 NULL
        // 레거시 자유 텍스트("0.38mm/Clear" 등) 는 영구 제거. 정책상 itemSpec 은 W×D×H derived 만.
        int specCleaned = 0;
        for (Item item : itemRepository.findAll()) {
            String desired = (item.getItemWidth() != null && item.getItemDepth() != null && item.getItemHeight() != null)
                    ? assembleSpec(item.getItemWidth(), item.getItemDepth(), item.getItemHeight())
                    : null;
            if (java.util.Objects.equals(desired, item.getItemSpec())) continue;
            item.setItemSpec(desired); // updateInfo 는 null 무시 → null set 가능한 직접 setter 사용
            itemRepository.save(item);
            specCleaned++;
        }

        if (canonicalApplied > 0 || specCleaned > 0) {
            itemRepository.flush();
            log.info("ItemSpecBackfillBootstrap: canonical W/D/H applied to {} items, itemSpec normalized for {} items",
                    canonicalApplied, specCleaned);
        }
    }

    private boolean matchesCanonical(Item it, ItemDimensions d) {
        return d.width().equals(it.getItemWidth())
                && d.depth().equals(it.getItemDepth())
                && d.height().equals(it.getItemHeight())
                && assembleSpec(d.width(), d.depth(), d.height()).equals(it.getItemSpec());
    }

    private record ItemDimensions(Integer width, Integer depth, Integer height) {}
}
