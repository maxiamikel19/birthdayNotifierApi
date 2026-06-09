package com.maxiamikel19.birthday_notifier_api.dto.request;

import java.time.LocalDate;

import com.maxiamikel19.birthday_notifier_api.entity.enums.MemberRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;

@Schema(description = "Solicitud para actualizar información de un miembro existente")
public record UpdateMemberRequest(

        @Schema(description = "Nuevo nombre del miembro", example = "Amikel") String firstName,

        @Schema(description = "Nuevo apellido del miembro", example = "Maxi") String lastName,

        @Schema(description = "Nuevo correo electrónico del miembro", example = "amikel.maxi@gmail.com") @Email(message = "Invalid email format") String email,

        @Schema(description = "Nuevo número de teléfono del miembro", example = "+56912345678") String phoneNumber,

        @Schema(description = "Nueva fecha de afiliación (no puede ser futura)", example = "2023-01-01") @PastOrPresent(message = "Affiliation date cannot be in the future") LocalDate affiliationDate,

        @Schema(description = "Nuevo rol del miembro dentro del sistema", example = "MEMBER") MemberRole memberRole,

        @Schema(description = "Nuevo rol del miembro dentro del sistema", example = "SOCIO_ACTIVO") Boolean status) {
}
