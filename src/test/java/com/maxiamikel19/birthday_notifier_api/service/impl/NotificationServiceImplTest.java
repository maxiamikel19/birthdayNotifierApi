package com.maxiamikel19.birthday_notifier_api.service.impl;

import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.entity.enums.Gender;
import com.maxiamikel19.birthday_notifier_api.repository.MemberRepository;
import com.maxiamikel19.birthday_notifier_api.service.IBirthdayService;
import com.maxiamikel19.birthday_notifier_api.service.IEmailService;
import com.maxiamikel19.birthday_notifier_api.service.IWhatsappService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Notification Service Tests")
class NotificationServiceImplTest {

    @Mock
    private IBirthdayService birthdayService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private IEmailService emailService;

    @Mock
    private IWhatsappService whatsappService;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Member birthdayMember;
    private Member recipientMember;

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(
                notificationService,
                "intervalDays",
                5);

        birthdayMember = createMember(
                "Juan",
                "Perez",
                "juan@gmail.com",
                true);

        recipientMember = createMember(
                "Maria",
                "Garcia",
                "maria@gmail.com",
                true);
    }

    @Test
    @DisplayName("Should notify current birthdays")
    void shouldNotifyCurrentBirthdays() {

        when(birthdayService.getCurrentBirthdays())
                .thenReturn(List.of(birthdayMember));

        when(memberRepository.findByIdNotIn(anyList()))
                .thenReturn(List.of(recipientMember));

        notificationService.notifyCurrentBirthdays();

        verify(emailService, times(1))
                .sendCurrentBirthdayEmails(
                        eq(recipientMember),
                        anyString());

        verify(whatsappService, times(1))
                .sendCurrentBirthdayWhatsapp(
                        eq(recipientMember),
                        anyString());
    }

    @Test
    @DisplayName("Should not notify when no current birthdays")
    void shouldNotNotifyWhenNoCurrentBirthdays() {

        when(birthdayService.getCurrentBirthdays())
                .thenReturn(List.of());

        notificationService.notifyCurrentBirthdays();

        verifyNoInteractions(emailService);
        verifyNoInteractions(whatsappService);
    }

    @Test
    @DisplayName("Should notify upcoming birthdays")
    void shouldNotifyUpcomingBirthdays() {

        when(birthdayService.getUpcomingBirthdays())
                .thenReturn(List.of(birthdayMember));

        when(memberRepository.findByIdNotIn(anyList()))
                .thenReturn(List.of(recipientMember));

        notificationService.notifyUpcomingBirthdays();

        verify(emailService, times(1))
                .sendUpcomingBirthdayEmails(
                        eq(recipientMember),
                        anyString(),
                        eq(5));

        verify(whatsappService, times(1))
                .sendUpcomingBirthdayWhatsapp(
                        eq(recipientMember),
                        anyString(),
                        eq(5));
    }

    @Test
    @DisplayName("Should not notify when no upcoming birthdays")
    void shouldNotNotifyWhenNoUpcomingBirthdays() {

        when(birthdayService.getUpcomingBirthdays())
                .thenReturn(List.of());

        notificationService.notifyUpcomingBirthdays();

        verifyNoInteractions(emailService);
        verifyNoInteractions(whatsappService);
    }

    @Test
    @DisplayName("Should send birthday congratulations")
    void shouldSendBirthdayCongratulations() {

        when(birthdayService.getCurrentBirthdays())
                .thenReturn(List.of(birthdayMember));

        notificationService.notifyBirthdaysCongratulations();

        verify(emailService, times(1))
                .sendBirthdaysCongratulationEmails(birthdayMember);

        verify(whatsappService, times(1))
                .sendBirthdaysCongratulationWhatsapp(birthdayMember);
    }

    @Test
    @DisplayName("Should not send congratulations when no birthdays")
    void shouldNotSendCongratulationsWhenNoBirthdays() {

        when(birthdayService.getCurrentBirthdays())
                .thenReturn(List.of());

        notificationService.notifyBirthdaysCongratulations();

        verifyNoInteractions(emailService);
        verifyNoInteractions(whatsappService);
    }

    @Test
    @DisplayName("Should not notify inactive members")
    void shouldNotNotifyInactiveMembers() {

        Member inactiveMember = createMember(
                "Carlos",
                "Lopez",
                "carlos@gmail.com",
                false);

        when(birthdayService.getCurrentBirthdays())
                .thenReturn(List.of(birthdayMember));

        when(memberRepository.findByIdNotIn(anyList()))
                .thenReturn(List.of(inactiveMember));

        notificationService.notifyCurrentBirthdays();

        verifyNoInteractions(emailService);
        verifyNoInteractions(whatsappService);
    }

    private Member createMember(
            String firstName,
            String lastName,
            String email,
            Boolean active) {

        return Member.builder()
                .id(UUID.randomUUID())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber("3001234567")
                .affiliationNumber("AFF001")
                .birthDate(LocalDate.of(1990, 5, 10))
                .gender(Gender.MALE)
                .active(active)
                .build();
    }
}
