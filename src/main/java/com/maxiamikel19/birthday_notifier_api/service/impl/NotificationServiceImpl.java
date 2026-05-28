package com.maxiamikel19.birthday_notifier_api.service.impl;

import java.util.List;

import org.hibernate.annotations.RowId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.repository.MemberRepository;
import com.maxiamikel19.birthday_notifier_api.service.IBirthdayService;
import com.maxiamikel19.birthday_notifier_api.service.IEmailService;
import com.maxiamikel19.birthday_notifier_api.service.INotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements INotificationService {

    private final IBirthdayService birthdayService;
    private final MemberRepository memberRepository;
    private final IEmailService emailService;

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
                log.info("Notifying to: %s: Today is the birthday of: %s".formatted(recipient.getFirstName(),
                        birthdayNames));
                emailService.sendCurrentBirthdayEmails(recipient, birthdayNames);
            }
        }

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
                log.info("Notifying to: %s about upcoming birthdays: %s".formatted(recipient.getFullName(),
                        birthdayNames));
                emailService.sendUpcomingBirthdayEmails(recipient, birthdayNames, intervalDays);
            }
        }
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
                log.info("Hi %s, great to wish you a happy birthday!".formatted(recipient.getFullName()));
                emailService.sendBirthdaysCongratulationEmails(recipient);
            }
        }
    }

    private List<Member> findRecipientExcludingBirthdayUsers(List<Member> birthdayMembers) {
        return memberRepository.findByIdNotIn(birthdayMembers.stream().map(Member::getId).toList());
    }

    private String getBirthdayNames(List<Member> birthdays) {
        return birthdays.stream()
                .map(Member::getFullName)
                .toList()
                .toString();
    }
}
