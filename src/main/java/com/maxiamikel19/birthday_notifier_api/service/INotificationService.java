package com.maxiamikel19.birthday_notifier_api.service;

public interface INotificationService {

    void notifyCurrentBirthdays();

    void notifyUpcomingBirthdays();

    void notifyBirthdaysCongratulations();
}
