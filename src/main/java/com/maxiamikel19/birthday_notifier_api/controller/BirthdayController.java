package com.maxiamikel19.birthday_notifier_api.controller;

import com.maxiamikel19.birthday_notifier_api.dto.response.MemberResponse;
import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.mapper.MemberMapper;
import com.maxiamikel19.birthday_notifier_api.service.IBirthdayService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/birthdays")
@RequiredArgsConstructor
@Tag(name = "Birthdays", description = "Endpoints para consultar cumpleaños actuales, próximos y del mes en curso.")
public class BirthdayController {

        private final IBirthdayService birthdayService;
        private final MemberMapper memberMapper;

        @Operation(summary = "Obtener cumpleaños de hoy", description = "Retorna todos los miembros cuya fecha de cumpleaños coincide con la fecha actual.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Cumpleaños obtenidos correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MemberResponse.class))))
        })
        @GetMapping("/today")
        public ResponseEntity<List<MemberResponse>> getCurrentBirthdays() {
                return ResponseEntity.ok().body(
                                mapToResponse(
                                                birthdayService.getCurrentBirthdays()));
        }

        @Operation(summary = "Obtener próximos cumpleaños", description = "Retorna los miembros cuyos cumpleaños ocurrirán próximamente.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Próximos cumpleaños obtenidos correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MemberResponse.class))))
        })
        @GetMapping("/upcoming")
        public ResponseEntity<List<MemberResponse>> getUpcomingBirthdays() {
                return ResponseEntity.ok().body(
                                mapToResponse(
                                                birthdayService.getUpcomingBirthdays()));
        }

        @Operation(summary = "Obtener cumpleaños del mes actual", description = "Retorna todos los miembros cuya fecha de cumpleaños pertenece al mes actual.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Cumpleaños del mes obtenidos correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MemberResponse.class))))
        })
        @GetMapping("/this-month")
        public ResponseEntity<List<MemberResponse>> getAllMonthBirthdays() {
                return ResponseEntity.ok().body(
                                mapToResponse(
                                                birthdayService.getBirthdaysThisMonth()));
        }

        private List<MemberResponse> mapToResponse(List<Member> members) {
                return members.stream()
                                .map(memberMapper::toResponse)
                                .toList();
        }
}
