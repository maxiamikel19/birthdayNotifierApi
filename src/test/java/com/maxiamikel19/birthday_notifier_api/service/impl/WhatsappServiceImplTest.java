package com.maxiamikel19.birthday_notifier_api.service.impl;

import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.entity.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Whatsapp Service Tests")
class WhatsappServiceImplTest {

    @Mock
    private RestClient.Builder restClientBuilder;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private WhatsappServiceImpl whatsappService;

    private Member member;

    @BeforeEach
    void setUp() {

        when(restClientBuilder.build()).thenReturn(restClient);

        whatsappService = new WhatsappServiceImpl(restClientBuilder);

        ReflectionTestUtils.setField(
                whatsappService,
                "baseUrl",
                "https://api.callmebot.com");

        ReflectionTestUtils.setField(
                whatsappService,
                "apiKey",
                "123456");

        ReflectionTestUtils.setField(
                whatsappService,
                "intervalDays",
                5);

        member = createMember();
    }

    @Test
    @DisplayName("Should send current birthday whatsapp")
    void shouldSendCurrentBirthdayWhatsapp() {

        mockRestClient();

        whatsappService.sendCurrentBirthdayWhatsapp(
                member,
                "Maria Lopez");

        verify(restClient, times(1)).get();
    }

    @Test
    @DisplayName("Should send upcoming birthday whatsapp")
    void shouldSendUpcomingBirthdayWhatsapp() {

        mockRestClient();

        whatsappService.sendUpcomingBirthdayWhatsapp(
                member,
                "Carlos Perez",
                5);

        verify(restClient, times(1)).get();
    }

    @Test
    @DisplayName("Should send congratulations whatsapp")
    void shouldSendCongratulationsWhatsapp() {

        mockRestClient();

        whatsappService.sendBirthdaysCongratulationWhatsapp(member);

        verify(restClient, times(1)).get();
    }

    @Test
    @DisplayName("Should handle exception when whatsapp sending fails")
    void shouldHandleWhatsappException() {

        when(restClient.get()).thenThrow(new RuntimeException("Connection error"));

        whatsappService.sendBirthdaysCongratulationWhatsapp(member);

        verify(restClient, times(1)).get();
    }

    private void mockRestClient() {

        when(restClient.get())
                .thenReturn(requestHeadersUriSpec);

        when(requestHeadersUriSpec.uri(anyString()))
                .thenReturn(requestHeadersUriSpec);

        when(requestHeadersUriSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.body(String.class))
                .thenReturn("Message sent");
    }

    private Member createMember() {

        return Member.builder()
                .id(UUID.randomUUID())
                .firstName("Juan")
                .lastName("Perez")
                .email("juan@gmail.com")
                .phoneNumber("+56 9 2872 0434")
                .birthDate(LocalDate.of(1995, 5, 20))
                .gender(Gender.MALE)
                .active(true)
                .build();
    }
}