package com.maxiamikel19.birthday_notifier_api.dto.request;

public record MemberFilter(
        String email,
        String firstName,
        String phoneNumber,
        String gender,
        Integer month
) {
}
