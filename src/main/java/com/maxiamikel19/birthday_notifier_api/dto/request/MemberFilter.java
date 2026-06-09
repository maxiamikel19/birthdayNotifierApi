package com.maxiamikel19.birthday_notifier_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filtros disponibles para búsqueda de miembros")
public record MemberFilter(

                @Schema(description = "Filtrar por correo electrónico del miembro", example = "amikel.maxi@gmail.com") String email,
                @Schema(description = "Filtrar por nombre del miembro", example = "Amikel") String firstName,
                @Schema(description = "Filtrar por número de teléfono", example = "+56912345678") String phoneNumber,
                @Schema(description = "Filtrar por género del miembro", example = "MALE") String gender,
                @Schema(description = "Filtrar miembros por mes de cumpleaños (1-12)", example = "5", minimum = "1", maximum = "12") Integer month) {
}
