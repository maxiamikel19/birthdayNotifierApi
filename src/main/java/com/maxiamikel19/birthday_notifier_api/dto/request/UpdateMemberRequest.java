package com.maxiamikel19.birthday_notifier_api.dto.request;

import java.time.LocalDate;

import com.maxiamikel19.birthday_notifier_api.entity.enums.MemberRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;

public record UpdateMemberRequest(

                String firstName,

                String lastName,

                @Email(message = "Invalid email format") String email,

                String phoneNumber,

                @PastOrPresent(message = "Affiliation date cannot be in the future") LocalDate affiliationDate,

                MemberRole memberRole,

                Boolean status) {
}
