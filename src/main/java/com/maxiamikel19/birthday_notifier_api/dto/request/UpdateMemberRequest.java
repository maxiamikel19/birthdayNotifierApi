package com.maxiamikel19.birthday_notifier_api.dto.request;

import com.maxiamikel19.birthday_notifier_api.entity.enums.Gender;
import com.maxiamikel19.birthday_notifier_api.entity.enums.MemberRole;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UpdateMemberRequest(

        String firstName,

        String lastName,

        @Email(message = "Invalid email format")
        String email,

        String phoneNumber,

        @PastOrPresent(message = "Affiliation date cannot be in the future")
        LocalDate affiliationDate,

        MemberRole memberRole,

        Boolean status
) {
}
