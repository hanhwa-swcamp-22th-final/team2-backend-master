package com.team2.master.command.application.service;

import com.team2.master.command.application.dto.CreateBuyerRequest;
import com.team2.master.command.application.dto.UpdateBuyerRequest;
import com.team2.master.command.domain.entity.Buyer;
import com.team2.master.command.domain.entity.Client;
import com.team2.master.command.infrastructure.client.ActivityFeignClient;
import com.team2.master.command.infrastructure.client.AuthFeignClient;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.domain.repository.BuyerRepository;
import com.team2.master.command.domain.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuyerCommandService {

    private final BuyerRepository buyerRepository;
    private final ClientRepository clientRepository;
    private final ActivityFeignClient activityFeignClient;
    private final AuthFeignClient authFeignClient;

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

        // Activity Contact 동기화 — 거래처 부서의 영업 담당자들 각각에게 생성
        syncToActivityContacts(saved);

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
     * Buyer 생성을 Activity Contact 에 동기화.
     * 거래처에 할당된 팀의 영업 담당자(sales) 전원에게 Contact 생성.
     * Activity/Auth 호출 실패 시 Buyer 저장은 유지 (best-effort). 수정/삭제는 sync 안 함.
     */
    private void syncToActivityContacts(Buyer buyer) {
        if (internalApiToken == null || internalApiToken.isBlank()) {
            log.warn("INTERNAL_API_TOKEN 미설정 — Activity contact 동기화 skip");
            return;
        }

        Integer teamId = buyer.getClient().getTeamId();
        if (teamId == null) {
            log.info("거래처 팀 미지정 — contact 동기화 skip [buyerId={}]", buyer.getBuyerId());
            return;
        }

        // 동기화 대상 = 거래처 팀의 영업 담당자 + 전사 ADMIN.
        // 4차 QA: 최관리(ADMIN) 컨택 리스트에 새 바이어가 안 뜬다는 보고 반영.
        List<AuthFeignClient.UserRef> targets = new ArrayList<>();
        Set<Integer> seenUserIds = new HashSet<>();
        try {
            List<AuthFeignClient.UserRef> salesUsers = authFeignClient.getUsersByRole(
                    internalApiToken, "sales", "active", teamId, null);
            if (salesUsers != null) {
                for (AuthFeignClient.UserRef u : salesUsers) {
                    if (u.userId() != null && seenUserIds.add(u.userId())) targets.add(u);
                }
            }
        } catch (Exception e) {
            log.warn("Auth sales 조회 실패 — sales sync skip [buyerId={}, reason={}]",
                    buyer.getBuyerId(), e.getMessage());
        }
        try {
            List<AuthFeignClient.UserRef> adminUsers = authFeignClient.getUsersByRole(
                    internalApiToken, "admin", "active", null, null);
            if (adminUsers != null) {
                for (AuthFeignClient.UserRef u : adminUsers) {
                    if (u.userId() != null && seenUserIds.add(u.userId())) targets.add(u);
                }
            }
        } catch (Exception e) {
            log.warn("Auth admin 조회 실패 — admin sync skip [buyerId={}, reason={}]",
                    buyer.getBuyerId(), e.getMessage());
        }

        if (targets.isEmpty()) {
            log.info("거래처 팀({}) sales + ADMIN 대상 없음 — contact 동기화 skip [buyerId={}]",
                    teamId, buyer.getBuyerId());
            return;
        }

        int successCount = 0;
        for (AuthFeignClient.UserRef user : targets) {
            if (user.userId() == null) continue;
            try {
                ActivityFeignClient.ContactInternalRequest payload =
                        new ActivityFeignClient.ContactInternalRequest(
                                user.userId().longValue(),
                                buyer.getBuyerName(),
                                buyer.getBuyerPosition(),
                                buyer.getBuyerEmail(),
                                buyer.getBuyerTel()
                        );
                activityFeignClient.createContactInternal(internalApiToken, payload);
                successCount++;
            } catch (Exception e) {
                log.warn("Activity contact sync 실패 [buyerId={}, userId={}, reason={}]",
                        buyer.getBuyerId(), user.userId(), e.getMessage());
            }
        }
        log.info("Activity contact sync 완료 [buyerId={}, success={}/{}]",
                buyer.getBuyerId(), successCount, targets.size());
    }
}
