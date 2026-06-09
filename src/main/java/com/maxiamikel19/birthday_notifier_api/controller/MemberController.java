package com.maxiamikel19.birthday_notifier_api.controller;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxiamikel19.birthday_notifier_api.dto.request.MemberCreateRequest;
import com.maxiamikel19.birthday_notifier_api.dto.request.MemberFilter;
import com.maxiamikel19.birthday_notifier_api.dto.request.UpdateMemberRequest;
import com.maxiamikel19.birthday_notifier_api.dto.response.MemberResponse;
import com.maxiamikel19.birthday_notifier_api.dto.response.PageResponse;
import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.mapper.MemberMapper;
import com.maxiamikel19.birthday_notifier_api.service.IMemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Members", description = "Gestión de miembros")
@Slf4j
public class MemberController {

        private final IMemberService memberService;
        private final MemberMapper memberMapper;

        @Operation(summary = "Crear un miembro")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Miembro creado correctamente"),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                        @ApiResponse(responseCode = "409", description = "Miembro ya existe")
        })
        @PostMapping
        public ResponseEntity<MemberResponse> create(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del nuevo mienbro", required = true, content = @Content(schema = @Schema(implementation = MemberCreateRequest.class))) @Valid @RequestBody MemberCreateRequest request) {
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(memberMapper.toResponse(memberService.create(request)));
        }

        @Operation(summary = "Obtener miembro por ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Miembro encontrado"),
                        @ApiResponse(responseCode = "404", description = "Miembro no encontrado")
        })
        @GetMapping("/{id}")
        public ResponseEntity<MemberResponse> findById(
                        @Parameter(description = "UUID del mienbro", example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID id) {
                return ResponseEntity.ok(memberMapper.toResponse(memberService.findById(id)));
        }

        @Operation(summary = "Buscar miembros con paginación y filtros")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Listado de miembros")
        })
        @GetMapping
        public ResponseEntity<PageResponse<MemberResponse>> searchMembers(
                        @Parameter(description = "Paginación y ordenamiento") @ParameterObject MemberFilter filter,
                        @PageableDefault(size = 2, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {

                Page<Member> members = memberService.searchMembers(filter, pageable);
                List<MemberResponse> memberList = members
                                .stream()
                                .map(memberMapper::toResponse).toList();
                PageResponse<MemberResponse> response = new PageResponse<MemberResponse>(
                                memberList,
                                members.getNumber(),
                                members.getSize(),
                                members.getTotalElements(),
                                members.getTotalPages(),
                                members.isLast());

                log.info("Filter: {}", filter);
                log.info("Pageable: {}", pageable);
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Actualizar miembro parcialmente")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Miembro actualizado"),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                        @ApiResponse(responseCode = "404", description = "Miembro no encontrado")
        })
        @PatchMapping("/{id}")
        public ResponseEntity<MemberResponse> update(
                        @Parameter(description = "UUID del miembro") @PathVariable UUID id,
                        @RequestBody @Valid UpdateMemberRequest request) {
                return ResponseEntity.ok(memberMapper.toResponse(memberService.update(id, request)));
        }

        @Operation(summary = "Eliminar miembro")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Miembro eliminado"),
                        @ApiResponse(responseCode = "404", description = "UUID no encontrado") })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(
                        @PathVariable UUID id) {
                memberService.delete(id);
                return ResponseEntity.noContent().build();
        }
}
