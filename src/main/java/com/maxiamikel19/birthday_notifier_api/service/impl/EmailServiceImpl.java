package com.maxiamikel19.birthday_notifier_api.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.service.IEmailService;
import com.maxiamikel19.birthday_notifier_api.utils.MessageUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String sender;

    @Override
    public void sendCurrentBirthdayEmails(Member recipient, String birthdayNames) {
        String fullName = recipient.getFullName();
        String message = MessageUtils.generateCurrentBirthdayMessage(fullName, birthdayNames);
        String messageSubject = "Today's Birthday Notification";
        String receiver = recipient.getEmail();

        sendEmail(receiver, messageSubject, message);
    }

    @Override
    public void sendUpcomingBirthdayEmails(Member recipient, String birthdayNames, int interval) {
        String fullName = recipient.getFullName();
        String message = MessageUtils.generateUpcomingMessage(fullName, birthdayNames, interval);
        String messageSubject = "Upcoming Birthday Notification";
        String receiver = recipient.getEmail();

        sendEmail(receiver, messageSubject, message);
    }

    @Override
    public void sendBirthdaysCongratulationEmails(Member member) {
        String fullName = member.getFullName();
        String message = MessageUtils.generateCongratulationsMessage(fullName);
        String messageSubject = "Birthday Congratulations";
        String receiver = member.getEmail();
        sendEmail(receiver, messageSubject, message);
    }

    private void sendEmail(String recipientEmail, String subject, String body) {
        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(recipientEmail);
            message.setFrom(sender);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            log.info("Email sent successfully to {}", recipientEmail);

        } catch (MailException e) {
            log.error("Failed to send email to {}", recipientEmail, e);
        }
    }

}
