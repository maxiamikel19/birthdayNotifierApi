package com.maxiamikel19.birthday_notifier_api.dto.request;

import com.maxiamikel19.birthday_notifier_api.entity.enums.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record MemberCreateRequest(
        @NotBlank(message = "First name is required") String firstName,

        @NotBlank(message = "Last name is required") String lastName,

        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,

        @NotBlank(message = "Phone number is required") String phoneNumber,

        @NotNull(message = "Birth date is required") @Past(message = "Birth date must be in the past") LocalDate birthDate,

        @NotNull(message = "Gender is required") Gender gender,

        @PastOrPresent(message = "Affiliation date cannot be in the future") LocalDate affiliationDate
) {
}
