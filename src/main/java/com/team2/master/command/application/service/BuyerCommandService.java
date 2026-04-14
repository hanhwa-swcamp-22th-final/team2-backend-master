package com.team2.master.command.application.service;

import com.team2.master.command.application.dto.CreateBuyerRequest;
import com.team2.master.command.application.dto.UpdateBuyerRequest;
import com.team2.master.command.domain.entity.Buyer;
import com.team2.master.command.domain.entity.Client;
import com.team2.master.command.infrastructure.client.ActivityFeignClient;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.domain.repository.BuyerRepository;
import com.team2.master.command.domain.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuyerCommandService {

    private final BuyerRepository buyerRepository;
    private final ClientRepository clientRepository;
    private final ActivityFeignClient activityFeignClient;

    @Value("${internal.api.token:}")
    private String internalApiToken;

    @Transactional
    public Buyer createBuyer(CreateBuyerRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("거래처를 찾을 수 없습니다."));

        Buyer buyer = Buyer.builder()
                .client(client)
                .buyerName(request.getBuyerName())
                .buyerPosition(request.getBuyerPosition())
                .buyerEmail(request.getBuyerEmail())
                .buyerTel(request.getBuyerTel())
                .build();

        Buyer saved = buyerRepository.save(buyer);

        // Activity 의 Contact 에 best-effort 동기화. 실패해도 Buyer 생성은 성공.
        syncToActivityContact(saved);

        return saved;
    }

    @Transactional
    public Buyer updateBuyer(Integer id, UpdateBuyerRequest request) {
        Buyer buyer = buyerRepository.findByIdWithClient(id)
                .orElseThrow(() -> new ResourceNotFoundException("바이어를 찾을 수 없습니다."));
        buyer.updateInfo(
                request.getBuyerName(), request.getBuyerPosition(),
                request.getBuyerEmail(), request.getBuyerTel()
        );
        return buyer;
    }

    @Transactional
    public void deleteBuyer(Integer id) {
        Buyer buyer = buyerRepository.findByIdWithClient(id)
                .orElseThrow(() -> new ResourceNotFoundException("바이어를 찾을 수 없습니다."));
        buyerRepository.delete(buyer);
    }

    /**
     * Master 의 Buyer 생성을 Activity 의 Contact 에 동기화.
     * Activity 호출 실패 시 Buyer 저장은 유지 (best-effort). 수정/삭제는 sync 안 함.
     */
    private void syncToActivityContact(Buyer buyer) {
        if (internalApiToken == null || internalApiToken.isBlank()) {
            log.warn("INTERNAL_API_TOKEN 미설정 — Activity contact 동기화 skip");
            return;
        }
        try {
            ActivityFeignClient.ContactInternalRequest payload =
                    new ActivityFeignClient.ContactInternalRequest(
                            buyer.getClient().getClientId().longValue(),
                            // Buyer 는 사용자 컨텍스트 없이 생성 가능하므로 시스템 writerId = 0 사용
                            0L,
                            buyer.getBuyerName(),
                            buyer.getBuyerPosition(),
                            buyer.getBuyerEmail(),
                            buyer.getBuyerTel()
                    );
            activityFeignClient.createContactInternal(internalApiToken, payload);
            log.info("Activity contact sync 성공 [buyerId={}, clientId={}]",
                    buyer.getBuyerId(), buyer.getClient().getClientId());
        } catch (Exception e) {
            log.warn("Activity contact sync 실패 [buyerId={}, reason={}] — Buyer 저장은 정상",
                    buyer.getBuyerId(), e.getMessage());
        }
    }
}
