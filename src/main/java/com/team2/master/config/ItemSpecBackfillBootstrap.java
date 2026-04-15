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
 * 운영 DB item_spec 자유 텍스트가 일관 포맷이 아니어서 PDF/주문서 표시 품질 저하.
 * 물리 치수가 의미있는 품목(솔라모듈/유리 등)에 대해 W/D/H + 정돈된 itemSpec 으로 1회 upsert.
 *
 * 정책:
 * - 매핑된 item_code 의 W/D/H 와 itemSpec 을 canonical 값으로 강제 설정 (idempotent)
 * - 매핑되지 않은 item_code 는 무시 (cable, sealant 등 W×D×H 가 의미 없는 품목)
 * - 사용자가 admin 화면에서 수정한 값도 매번 재시작마다 canonical 로 되돌아감 — 운영팀이
 *   admin 화면에서 자유롭게 편집하려면 매핑 제거 또는 ENV flag 도입 필요 (현재 시드 정합성 우선)
 */
@Component
public class ItemSpecBackfillBootstrap {

    private static final Logger log = LoggerFactory.getLogger(ItemSpecBackfillBootstrap.class);

    /** 물리 치수가 의미있는 품목의 canonical W/D/H + 정돈된 spec. INT 컬럼이라 mm 단위 정수. */
    private static final Map<String, ItemDimensions> CANONICAL = new LinkedHashMap<>();
    static {
        // 솔라모듈 — 시중 표준 두께 35mm 가정
        CANONICAL.put("ITM010", new ItemDimensions(1722, 1134, 35, "Mono PERC 400W"));
        CANONICAL.put("ITM011", new ItemDimensions(2094, 1134, 35, "Mono PERC 500W"));
        CANONICAL.put("ITM012", new ItemDimensions(2384, 1303, 35, "Mono PERC 600W"));
        // 강화유리 — 두께 3.2mm 는 INT 이라 3mm 로 절사
        CANONICAL.put("ITM006", new ItemDimensions(1722, 1134, 3, "Tempered AR Coated Low Iron"));
    }

    private final ItemRepository itemRepository;

    public ItemSpecBackfillBootstrap(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void normalizeOnReady() {
        int updated = 0;
        for (Map.Entry<String, ItemDimensions> entry : CANONICAL.entrySet()) {
            Item item = itemRepository.findByItemCode(entry.getKey()).orElse(null);
            if (item == null) continue;
            ItemDimensions d = entry.getValue();
            if (matchesCanonical(item, d)) continue;
            item.updateInfo(
                    null, null, d.spec(),
                    d.width(), d.depth(), d.height(),
                    null, null, null, null, null, null
            );
            itemRepository.save(item);
            updated++;
        }
        if (updated > 0) {
            itemRepository.flush();
            log.info("ItemSpecBackfillBootstrap: normalized W/D/H + spec for {} items", updated);
        }
    }

    private boolean matchesCanonical(Item it, ItemDimensions d) {
        return d.width().equals(it.getItemWidth())
                && d.depth().equals(it.getItemDepth())
                && d.height().equals(it.getItemHeight())
                && d.spec().equals(it.getItemSpec());
    }

    private record ItemDimensions(Integer width, Integer depth, Integer height, String spec) {}
}
