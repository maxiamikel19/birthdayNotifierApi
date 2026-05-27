package com.maxiamikel19.birthday_notifier_api.exception;

public record StandardError(
        Integer codeStatus,
        Long timestamp,
        Object message
) {
}
