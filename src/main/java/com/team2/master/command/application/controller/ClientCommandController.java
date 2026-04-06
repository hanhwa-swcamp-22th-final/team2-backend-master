package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.ChangeStatusRequest;
import com.team2.master.query.dto.ClientResponse;
import com.team2.master.command.application.dto.CreateClientRequest;
import com.team2.master.command.application.dto.UpdateClientRequest;
import com.team2.master.command.domain.entity.Client;
import com.team2.master.command.domain.entity.enums.ClientStatus;
import com.team2.master.command.application.service.ClientCommandService;
import com.team2.master.query.controller.ClientQueryController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "거래처 Command", description = "거래처 등록/수정/상태변경 API")
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientCommandController {

    private final ClientCommandService clientCommandService;

    @Operation(summary = "거래처 등록", description = "새로운 거래처를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "거래처 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<EntityModel<ClientResponse>> createClient(@Valid @RequestBody CreateClientRequest request) {
        Client client = clientCommandService.createClient(request);
        ClientResponse response = ClientResponse.from(client);
        EntityModel<ClientResponse> model = EntityModel.of(response,
                linkTo(methodOn(ClientQueryController.class).getClient(client.getClientId())).withSelfRel(),
                linkTo(methodOn(ClientQueryController.class).getClients(null, null, null, null, 0, 10)).withRel("clients"));
        URI location = linkTo(methodOn(ClientQueryController.class).getClient(client.getClientId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @Operation(summary = "거래처 수정", description = "기존 거래처 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "거래처 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "거래처를 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ClientResponse>> updateClient(
            @Parameter(description = "거래처 ID") @PathVariable Integer id,
            @Valid @RequestBody UpdateClientRequest request) {
        Client client = clientCommandService.updateClient(id, request);
        return ResponseEntity.ok(EntityModel.of(ClientResponse.from(client),
                linkTo(methodOn(ClientQueryController.class).getClient(id)).withSelfRel(),
                linkTo(methodOn(ClientQueryController.class).getClients(null, null, null, null, 0, 10)).withRel("clients")));
    }

    @Operation(summary = "거래처 상태 변경", description = "거래처의 활성/비활성 상태를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 상태값"),
            @ApiResponse(responseCode = "404", description = "거래처를 찾을 수 없음")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<EntityModel<ClientResponse>> changeStatus(
            @Parameter(description = "거래처 ID") @PathVariable Integer id,
            @Valid @RequestBody ChangeStatusRequest request) {
        ClientStatus status = ClientStatus.valueOf(request.getStatus());
        Client client = clientCommandService.changeStatus(id, status);
        return ResponseEntity.ok(EntityModel.of(ClientResponse.from(client),
                linkTo(methodOn(ClientQueryController.class).getClient(id)).withSelfRel()));
    }
}
