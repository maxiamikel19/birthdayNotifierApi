package com.maxiamikel19.birthday_notifier_api.dto.request;

import com.maxiamikel19.birthday_notifier_api.entity.enums.Gender;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Solicitud para crear un nuevo miembro")
public record MemberCreateRequest(
                @Schema(description = "Nombre del miembro", example = "Amikel") @NotBlank(message = "First name is required") String firstName,

                @Schema(description = "Apellido del miembro", example = "Maxi") @NotBlank(message = "Last name is required") String lastName,

                @Schema(description = "Correo electrónico válido del miembro", example = "amikel.maxi@gmail.com") @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,

                @Schema(description = "Número de teléfono del miembro", example = "+56912345678") @NotBlank(message = "Phone number is required") String phoneNumber,

                @Schema(description = "Fecha de nacimiento del miembro (debe ser una fecha pasada)", example = "1998-04-15") @NotNull(message = "Birth date is required") @Past(message = "Birth date must be in the past") LocalDate birthDate,

                @Schema(description = "Género del miembro", example = "MALE") @NotNull(message = "Gender is required") Gender gender,

                @Schema(description = "Fecha de afiliación del miembro (no puede ser futura)", example = "2023-01-01") @PastOrPresent(message = "Affiliation date cannot be in the future") LocalDate affiliationDate) {
}
