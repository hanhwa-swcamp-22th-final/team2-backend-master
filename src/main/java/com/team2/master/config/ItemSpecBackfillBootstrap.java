package com.team2.master.config;

import com.team2.master.command.domain.entity.Item;
import com.team2.master.command.domain.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 운영 DB 의 자유 텍스트 item_spec 값을 파싱해 item_width/depth/height 컬럼을 idempotent 채움.
 *
 * 배경: ItemFormModal 은 W/D/H 를 분리 입력 + " [W × D × H] mm" 텍스트로 합성해 itemSpec 에도
 * 저장하나, 이전 시드 데이터(예: "1722×1134mm", "Mono/400W/1722x1134mm") 는 자유 텍스트만
 * 채워져 있어 W/D/H 컬럼이 NULL. PDF/주문서가 구조화된 치수 필드를 사용할 때 누락 방지.
 *
 * 매칭 규칙:
 *   - 3-part: 첫 매칭 NxNxN  → W, D, H
 *   - 2-part: 첫 매칭 NxN    → W, H (D 는 NULL 유지) — 패널/시트 류 관행
 *   - 매칭 없음               → 변경 없음 (예: "0.38mm/Clear", "IP68/3 Diodes/MC4")
 *
 * 사용자 커스텀 값(특정 W/D/H 가 이미 채워진 row)은 fillDimensionsIfMissing 로 보존.
 */
@Component
public class ItemSpecBackfillBootstrap {

    private static final Logger log = LoggerFactory.getLogger(ItemSpecBackfillBootstrap.class);

    /** 3-part W×D×H */
    private static final Pattern THREE_PART = Pattern.compile("(\\d+)\\s*[×xX]\\s*(\\d+)\\s*[×xX]\\s*(\\d+)");
    /** 2-part W×H (3-part 가 매칭 안 될 때만 사용) */
    private static final Pattern TWO_PART = Pattern.compile("(\\d+)\\s*[×xX]\\s*(\\d+)");

    private final ItemRepository itemRepository;

    public ItemSpecBackfillBootstrap(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void backfillOnReady() {
        List<Item> items = itemRepository.findAll();
        int updated = 0;
        for (Item item : items) {
            if (allDimensionsPresent(item)) continue;
            String spec = item.getItemSpec();
            if (spec == null || spec.isBlank()) continue;

            Integer[] dims = parseDimensions(spec);
            if (dims == null) continue;

            item.fillDimensionsIfMissing(dims[0], dims[1], dims[2]);
            itemRepository.save(item);
            updated++;
        }
        if (updated > 0) {
            itemRepository.flush();
            log.info("ItemSpecBackfillBootstrap: filled W/D/H for {} items", updated);
        }
    }

    private boolean allDimensionsPresent(Item item) {
        return item.getItemWidth() != null && item.getItemDepth() != null && item.getItemHeight() != null;
    }

    /** @return [W, D, H] (2-part 매칭일 때 D=null) 또는 null (매칭 없음) */
    private Integer[] parseDimensions(String spec) {
        Matcher m3 = THREE_PART.matcher(spec);
        if (m3.find()) {
            return new Integer[]{
                    Integer.parseInt(m3.group(1)),
                    Integer.parseInt(m3.group(2)),
                    Integer.parseInt(m3.group(3))
            };
        }
        Matcher m2 = TWO_PART.matcher(spec);
        if (m2.find()) {
            return new Integer[]{
                    Integer.parseInt(m2.group(1)),
                    null, // 2-part 는 D 미상
                    Integer.parseInt(m2.group(2))
            };
        }
        return null;
    }
}
