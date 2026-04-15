package com.team2.master.command.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Master → Activity 내부 호출 Feign Client.
 * Buyer 생성 시 Activity 의 Contact 동기화 용도.
 */
@FeignClient(
        name = "activity-contacts",
        url = "${activity.service.url:http://backend-activity:8013}",
        configuration = FeignInternalConfig.class
)
public interface ActivityFeignClient {

    /**
     * Master 의 Buyer 생성 시 Activity 의 Contact 도 함께 생성.
     * X-Internal-Token 헤더로 Activity 의 InternalApiTokenFilter 가 검증.
     */
    @PostMapping("/api/contacts/internal")
    void createContactInternal(
            @RequestHeader("X-Internal-Token") String internalToken,
            @RequestBody ContactInternalRequest request
    );

    record ContactInternalRequest(
            Long writerId,
            String contactName,
            String contactPosition,
            String contactEmail,
            String contactTel
    ) {}
}
