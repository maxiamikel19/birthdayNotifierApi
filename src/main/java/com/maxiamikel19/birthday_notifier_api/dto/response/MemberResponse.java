package com.maxiamikel19.birthday_notifier_api.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record MemberResponse(
        UUID id,
        String fullName,
        String email,
        String phoneNumber,
        String affiliationNumber,
        String gender,
        String memberRole,
        String status,
        LocalDate affiliationDate,
        int age,
        int affiliationYears,
        Instant createdAt,
        Instant updatedAt
) {
}
