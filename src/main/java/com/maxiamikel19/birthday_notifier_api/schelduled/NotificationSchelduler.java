package com.maxiamikel19.birthday_notifier_api.schelduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.maxiamikel19.birthday_notifier_api.service.INotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class NotificationSchelduler {

    private final INotificationService notificationService;

    @Scheduled(cron = "${api.birthday.current-cron}")
    public void notifyCurrentBirthdays() {
        log.info("Starting birthday greetings");
        try {
            notificationService.notifyCurrentBirthdays();
            log.info("Finished birthday greetings");
        } catch (Exception ex) {

            log.error("Error sending birthday greetings", ex);
        }
    }

    @Scheduled(cron = "${api.birthday.upcomming-cron}")
    public void notifyUpcomingBirthdays() {
        log.info("Starting upcoming birthday notifications");
        try {
            notificationService.notifyUpcomingBirthdays();
            log.info("Finished upcoming birthday notifications");
        } catch (Exception ex) {
            log.error("Error sending upcoming birthday notifications", ex);
        }
    }

    @Scheduled(cron = "${api.birthday.congratulations-cron}")
    public void notifyBirthdaysCongratulations() {
        log.info("Starting birthday congratulations");
        try {
            notificationService.notifyCurrentBirthdays();
            log.info("Finished birthday congratulations");
        } catch (Exception ex) {
            log.error("Error sending birthday congratulations", ex);
        }
    }

}
