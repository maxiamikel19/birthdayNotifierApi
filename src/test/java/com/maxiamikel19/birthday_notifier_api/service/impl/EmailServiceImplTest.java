package com.maxiamikel19.birthday_notifier_api.service.impl;

import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.entity.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Email Service Tests")
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    private Member member;

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(
                emailService,
                "sender",
                "test@gmail.com");

        member = createMember(
                "Juan",
                "Perez",
                "juan@gmail.com");
    }

    @Test
    @DisplayName("Should send current birthday email")
    void shouldSendCurrentBirthdayEmail() {

        String birthdayNames = "Maria Garcia";

        emailService.sendCurrentBirthdayEmails(
                member,
                birthdayNames);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender, times(1))
                .send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();

        assertEquals("juan@gmail.com", sentMessage.getTo()[0]);
        assertEquals("test@gmail.com", sentMessage.getFrom());
        assertEquals("Today's Birthday Notification", sentMessage.getSubject());
        assertNotNull(sentMessage.getText());
    }

    @Test
    @DisplayName("Should send upcoming birthday email")
    void shouldSendUpcomingBirthdayEmail() {

        emailService.sendUpcomingBirthdayEmails(
                member,
                "Carlos Lopez",
                5);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender, times(1))
                .send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();

        assertEquals("juan@gmail.com", sentMessage.getTo()[0]);
        assertEquals("Upcoming Birthday Notification", sentMessage.getSubject());
        assertNotNull(sentMessage.getText());
    }

    @Test
    @DisplayName("Should send birthday congratulations email")
    void shouldSendBirthdayCongratulationsEmail() {

        emailService.sendBirthdaysCongratulationEmails(member);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender, times(1))
                .send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();

        assertEquals("juan@gmail.com", sentMessage.getTo()[0]);
        assertEquals("Birthday Congratulations", sentMessage.getSubject());
        assertNotNull(sentMessage.getText());
    }

    @Test
    @DisplayName("Should handle email sending error")
    void shouldHandleEmailSendingError() {

        doThrow(new MailException("Error sending email") {
        }).when(mailSender).send(any(SimpleMailMessage.class));

        assertDoesNotThrow(() -> emailService.sendCurrentBirthdayEmails(
                member,
                "Maria Garcia"));

        verify(mailSender, times(1))
                .send(any(SimpleMailMessage.class));
    }

    private Member createMember(
            String firstName,
            String lastName,
            String email) {

        return Member.builder()
                .id(UUID.randomUUID())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber("3001234567")
                .affiliationNumber("AFF001")
                .birthDate(LocalDate.of(1990, 5, 10))
                .gender(Gender.MALE)
                .active(true)
                .build();
    }
}