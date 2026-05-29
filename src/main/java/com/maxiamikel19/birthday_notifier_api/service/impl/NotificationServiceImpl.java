package com.maxiamikel19.birthday_notifier_api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.repository.MemberRepository;
import com.maxiamikel19.birthday_notifier_api.service.IBirthdayService;
import com.maxiamikel19.birthday_notifier_api.service.IEmailService;
import com.maxiamikel19.birthday_notifier_api.service.INotificationService;
import com.maxiamikel19.birthday_notifier_api.service.IWhatsappService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements INotificationService {

    private final IBirthdayService birthdayService;
    private final MemberRepository memberRepository;
    private final IEmailService emailService;
    private final IWhatsappService whatsappService;

    @Value("${api.birthday.interval-days}")
    private int intervalDays;

    @Override
    public void notifyCurrentBirthdays() {

        List<Member> currentBirthdays = birthdayService.getCurrentBirthdays();
        if (currentBirthdays.isEmpty()) {
            log.info("No birthdays to notify for today.");
            return;
        }

        List<Member> recipients = findRecipientExcludingBirthdayUsers(currentBirthdays);
        String birthdayNames = getBirthdayNames(currentBirthdays);
        for (Member recipient : recipients) {
            if (recipient.getActive()) {
                emailService.sendCurrentBirthdayEmails(recipient, birthdayNames);
                whatsappService.sendCurrentBirthdayWhatsapp(recipient, birthdayNames);
            }
        }
        log.info("Notifying to: %s members".formatted(recipients.size()));
    }

    @Override
    public void notifyUpcomingBirthdays() {
        List<Member> upcomingBirthdays = birthdayService.getUpcomingBirthdays();
        if (upcomingBirthdays.isEmpty()) {
            log.info("No upcoming birthdays to notify.");
            return;
        }
        List<Member> recipients = findRecipientExcludingBirthdayUsers(upcomingBirthdays);
        String birthdayNames = getBirthdayNames(upcomingBirthdays);

        for (Member recipient : recipients) {
            if (recipient.getActive()) {
                emailService.sendUpcomingBirthdayEmails(recipient, birthdayNames, intervalDays);
                whatsappService.sendUpcomingBirthdayWhatsapp(recipient, birthdayNames, intervalDays);
            }
        }
        log.info("Notifying to: %s members".formatted(recipients.size()));
    }

    @Override
    public void notifyBirthdaysCongratulations() {
        List<Member> monthBirthdays = birthdayService.getCurrentBirthdays();
        if (monthBirthdays.isEmpty()) {
            log.info("No birthdays this month to notify.");
            return;
        }
        for (Member recipient : monthBirthdays) {
            if (recipient.getActive()) {
                emailService.sendBirthdaysCongratulationEmails(recipient);
                whatsappService.sendBirthdaysCongratulationWhatsapp(recipient);
            }
        }
        log.info("Notifying to: %s members".formatted(monthBirthdays.size()));
    }

    private List<Member> findRecipientExcludingBirthdayUsers(List<Member> birthdayMembers) {
        return memberRepository.findByIdNotIn(birthdayMembers.stream().map(Member::getId).toList());
    }

    private String getBirthdayNames(List<Member> birthdays) {
        return birthdays.stream()
                .map(Member::getFullName)
                .collect(Collectors.joining(" and "));
    }
}
