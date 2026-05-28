package com.maxiamikel19.birthday_notifier_api.service;

import com.maxiamikel19.birthday_notifier_api.entity.Member;

public interface IEmailService {

    void sendCurrentBirthdayEmails(Member recipient, String birthdayNames);

    void sendUpcomingBirthdayEmails(Member recipient, String birthdayNames, int interval);

    void sendBirthdaysCongratulationEmails(Member member);
}
