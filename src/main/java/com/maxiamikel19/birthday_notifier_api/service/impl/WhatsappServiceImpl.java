package com.maxiamikel19.birthday_notifier_api.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.service.IWhatsappService;
import com.maxiamikel19.birthday_notifier_api.utils.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WhatsappServiceImpl implements IWhatsappService {

    private final RestClient restClient;

    public WhatsappServiceImpl(RestClient.Builder restClient) {
        this.restClient = restClient.build();
    }

    @Value("${callmebot.base-url}")
    private String baseUrl;

    @Value("${callmebot.api-key}")
    private String apiKey;

    @Value("${api.birthday.interval-days}")
    private int intervalDays;

    @Override
    public void sendCurrentBirthdayWhatsapp(Member recipient, String birthdayNames) {
        String message = MessageUtils.generateCurrentBirthdayMessage(recipient.getFirstName(), birthdayNames);
        sendMessage(recipient.getPhoneNumber(), message);
    }

    @Override
    public void sendUpcomingBirthdayWhatsapp(Member recipient, String birthdayNames, int interval) {
        String message = MessageUtils.generateUpcomingMessage(recipient.getFirstName(), birthdayNames, intervalDays);
        sendMessage(recipient.getPhoneNumber(), message);
    }

    @Override
    public void sendBirthdaysCongratulationWhatsapp(Member recipient) {
        String message = MessageUtils.generateCongratulationsMessage(recipient.getFullName());
        sendMessage(recipient.getPhoneNumber(), message);

    }

    private void sendMessage(String phoneNumber, String message) {
        try {
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

            String url = "%s/whatsapp.php?phone=%s&text=%s&apikey=%s"
                    .formatted(
                            baseUrl,
                            normalizePhoneNumber(phoneNumber),
                            encodedMessage,
                            apiKey);

            log.info("Sending WhatsApp request: {}", url);

            String response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);

            log.info("WhatsApp response: {}", response);

        } catch (Exception ex) {

            log.error("WhatsApp sending failed", ex);
        }
    }

    private String normalizePhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("\\D", "");
    }

}
