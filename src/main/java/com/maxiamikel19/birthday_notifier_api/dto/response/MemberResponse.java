package com.maxiamikel19.birthday_notifier_api.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información pública de un miembro")
public record MemberResponse(
                @Schema(description = "Identificador único del miembro", example = "550e8400-e29b-41d4-a716-446655440000") UUID id,

                @Schema(description = "Nombre completo del miembro", example = "Amikel Maxi") String fullName,

                @Schema(description = "Correo electrónico del miembro", example = "amikel.maxi@gmail.com") String email,

                @Schema(description = "Número de teléfono del miembro", example = "+541123456789") String phoneNumber,

                @Schema(description = "Número de afiliación o matrícula del miembro", example = "19810603-C050") String affiliationNumber,

                @Schema(description = "Género del miembro", example = "MALE") String gender,

                @Schema(description = "Rol del miembro dentro de la organización", example = "MEMBER") String memberRole,

                @Schema(description = "Estado del miembro", example = "activated") String status,

                @Schema(description = "Fecha de afiliación del miembro", example = "2020-05-29") LocalDate affiliationDate,

                @Schema(description = "Edad calculada del miembro", example = "26") int age,

                @Schema(description = "Años de afiliación calculados", example = "6") int affiliationYears,

                @Schema(description = "Fecha de creación del registro", example = "2023-01-01T12:34:56.789Z") Instant createdAt,

                @Schema(description = "Fecha de última actualización del registro", example = "2023-05-01T09:21:43.123Z") Instant updatedAt) {

}
