package com.maxiamikel19.birthday_notifier_api.service;

import com.maxiamikel19.birthday_notifier_api.entity.Member;

public interface IWhatsappService {

    void sendCurrentBirthdayWhatsapp(Member recipient, String birthdayNames);

    void sendUpcomingBirthdayWhatsapp(Member recipient, String birthdayNames, int interval);

    void sendBirthdaysCongratulationWhatsapp(Member recipient);
}
