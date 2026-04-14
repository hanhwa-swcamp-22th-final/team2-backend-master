package com.team2.master.command.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Master → Auth 내부 호출 Feign Client.
 * 거래처 enrich + 바이어 sync 용도로 사용자/팀 정보 조회.
 */
@FeignClient(
        name = "auth-internal",
        url = "${auth.service.url:http://backend-auth:8011}",
        configuration = FeignInternalConfig.class
)
public interface AuthFeignClient {

    @GetMapping("/api/users/internal/by-role")
    List<UserRef> getUsersByRole(
            @RequestHeader("X-Internal-Token") String internalToken,
            @RequestParam("role") String role,
            @RequestParam(value = "userStatus", required = false, defaultValue = "active") String userStatus,
            @RequestParam(value = "teamId", required = false) Integer teamId,
            @RequestParam(value = "departmentId", required = false) Integer departmentId
    );

    @GetMapping("/api/teams/internal/by-ids")
    List<TeamBrief> getTeamsByIds(
            @RequestHeader("X-Internal-Token") String internalToken,
            @RequestParam("ids") List<Integer> ids
    );

    @GetMapping("/api/teams/internal/by-department")
    List<TeamBrief> getTeamsByDepartment(
            @RequestHeader("X-Internal-Token") String internalToken,
            @RequestParam("departmentId") Integer departmentId
    );

    record UserRef(
            Integer userId,
            String employeeNo,
            String userName,
            String userEmail,
            String userRole,
            Integer teamId,
            String teamName,
            Integer departmentId,
            String departmentName,
            String positionName,
            String userStatus
    ) {}

    record TeamBrief(
            Integer teamId,
            String teamName,
            Integer departmentId,
            String departmentName
    ) {}
}
