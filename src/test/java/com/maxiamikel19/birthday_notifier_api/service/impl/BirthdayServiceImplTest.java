package com.maxiamikel19.birthday_notifier_api.service.impl;

import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.entity.enums.Gender;
import com.maxiamikel19.birthday_notifier_api.repository.MemberRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Birthday Service Tests")
class BirthdayServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private BirthdayServiceImpl birthdayService;

    private Member member;

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(
                birthdayService,
                "intervalDays",
                30);

        member = createMember(
                "Juan",
                "Perez",
                LocalDate.now());
    }

    @Test
    @DisplayName("Should return current birthdays")
    void shouldReturnCurrentBirthdays() {

        LocalDate today = LocalDate.now();

        when(memberRepository.findBirthdayByMonthAndDay(
                today.getMonthValue(),
                today.getDayOfMonth())).thenReturn(List.of(member));

        List<Member> result = birthdayService.getCurrentBirthdays();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getFirstName());

        verify(memberRepository, times(1))
                .findBirthdayByMonthAndDay(
                        today.getMonthValue(),
                        today.getDayOfMonth());
    }

    @Test
    @DisplayName("Should return empty list when no birthdays today")
    void shouldReturnEmptyCurrentBirthdays() {

        LocalDate today = LocalDate.now();

        when(memberRepository.findBirthdayByMonthAndDay(
                today.getMonthValue(),
                today.getDayOfMonth())).thenReturn(List.of());

        List<Member> result = birthdayService.getCurrentBirthdays();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(memberRepository, times(1))
                .findBirthdayByMonthAndDay(
                        today.getMonthValue(),
                        today.getDayOfMonth());
    }

    @Test
    @DisplayName("Should return upcoming birthdays")
    void shouldReturnUpcomingBirthdays() {

        LocalDate futureDate = LocalDate.now().plusDays(30);

        Member upcomingMember = createMember(
                "Maria",
                "Garcia",
                futureDate);

        when(memberRepository.findBirthdayByMonthAndDay(
                futureDate.getMonthValue(),
                futureDate.getDayOfMonth())).thenReturn(List.of(upcomingMember));

        List<Member> result = birthdayService.getUpcomingBirthdays();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Maria", result.get(0).getFirstName());

        verify(memberRepository, times(1))
                .findBirthdayByMonthAndDay(
                        futureDate.getMonthValue(),
                        futureDate.getDayOfMonth());
    }

    @Test
    @DisplayName("Should return birthdays this month")
    void shouldReturnBirthdaysThisMonth() {

        int currentMonth = LocalDate.now().getMonthValue();

        Member secondMember = createMember(
                "Carlos",
                "Lopez",
                LocalDate.of(1995, currentMonth, 10));

        when(memberRepository.findBirthdaysByMonth(currentMonth))
                .thenReturn(List.of(member, secondMember));

        List<Member> result = birthdayService.getBirthdaysThisMonth();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(memberRepository, times(1))
                .findBirthdaysByMonth(currentMonth);
    }

    @Test
    @DisplayName("Should return empty list when no birthdays this month")
    void shouldReturnEmptyBirthdaysThisMonth() {

        int currentMonth = LocalDate.now().getMonthValue();

        when(memberRepository.findBirthdaysByMonth(currentMonth))
                .thenReturn(List.of());

        List<Member> result = birthdayService.getBirthdaysThisMonth();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(memberRepository, times(1))
                .findBirthdaysByMonth(currentMonth);
    }

    private Member createMember(
            String firstName,
            String lastName,
            LocalDate birthDate) {

        return Member.builder()
                .id(UUID.randomUUID())
                .firstName(firstName)
                .lastName(lastName)
                .email(firstName.toLowerCase() + "@gmail.com")
                .affiliationNumber("AFF001")
                .phoneNumber("3001234567")
                .birthDate(birthDate)
                .gender(Gender.MALE)
                .active(true)
                .build();
    }
}