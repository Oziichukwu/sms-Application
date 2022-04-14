package com.example.smsapplication.sms;

import com.example.smsapplication.data.models.Account;
import com.example.smsapplication.data.models.PhoneNumber;
import com.example.smsapplication.data.repositories.AccountRepository;
import com.example.smsapplication.data.repositories.PhoneNumberRepository;
import com.example.smsapplication.dtos.MessageDto;
import com.example.smsapplication.services.sms.SmsService;
import com.example.smsapplication.web.exceptions.SmsBusinessException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

@SpringBootTest
@Slf4j
public class SmsServiceTest {

    private final SmsService smsService;
    private final AccountRepository accountRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final RedisTemplate<String, String> redisTemplate;


    private final Gson gson = new Gson();
    MessageDto firstRequest;
    MessageDto secondRequest;

    @Autowired
    public SmsServiceTest(SmsService smsService, AccountRepository accountRepository,
                              PhoneNumberRepository phoneNumberRepository, RedisTemplate<String, String> redisTemplate) {
        this.smsService = smsService;
        this.accountRepository = accountRepository;
        this.phoneNumberRepository = phoneNumberRepository;
        this.redisTemplate = redisTemplate;
    }

    @BeforeEach
    void setUp() {
        phoneNumberRepository.deleteAll();

        Account firstAccount = accountRepository.save(
                new Account(1, "QWERDF", "User1"));

        Account secondAccount = accountRepository.save(
                new Account(2, "POIUYT", "User2"));

        PhoneNumber firstPhoneNumber = phoneNumberRepository.save(
                new PhoneNumber(1, "6475765856", firstAccount));

        PhoneNumber secondPhoneNumber = phoneNumberRepository.save(
                new PhoneNumber(2, "7576546567", secondAccount));

        PhoneNumber thirdPhoneNumber = phoneNumberRepository.save(
                new PhoneNumber(3, "112200339", secondAccount));

        PhoneNumber fourthPhoneNumber = phoneNumberRepository.save(
                new PhoneNumber(4, "0989876783", secondAccount));

        firstRequest = new MessageDto();
        firstRequest.setSmsSender("6475765856");
        firstRequest.setSmsReceiver("7576546567");
        firstRequest.setSmsBody("Hello, beautiful");

        secondRequest = new MessageDto();
        secondRequest.setSmsSender("112200339");
        secondRequest.setSmsReceiver("0989876783");
        secondRequest.setSmsBody("Hillarious!");
    }

    @Test
    @DisplayName("Test that sms can be sent")
    void testThatPhoneNumberCanSendMessage() throws IOException, SmsBusinessException {
        String json = gson.toJson(secondRequest);

        MessageDto response = smsService.sendSms(json);

        assertThat(response).isNotNull();
        assertThat(response.getSmsBody()).isEqualTo("Hillarious!");
        assertThat(response.getSmsReceiver()).isEqualTo("0989876783");
        assertThat(response.getSmsSender()).isEqualTo("112200339");
    }

    @Test
    @DisplayName("Test that api call cannot be more than 50 in 24hrs")
    void testThatRateLimiterWorksOnApiCalls() throws IOException, SmsBusinessException {
        MessageDto request = new MessageDto();
        request.setSmsSender("7576546567");
        request.setSmsReceiver("0989876783");
        request.setSmsBody("Bro, how far?");

        String json = gson.toJson(request);
        for (int apiCall = 1; apiCall <= 50; apiCall++) {
            smsService.sendSms(json);
        }
        assertThrows(SmsBusinessException.class, () -> smsService.sendSms(json));
    }

    @Test
    void cacheMessageThatContainsStopTest() {
        MessageDto request = new MessageDto();
        request.setSmsSender("112200339");
        request.setSmsReceiver("0989876783");
        request.setSmsBody("Heyo! STOP");


        StringBuffer sb = new StringBuffer();
        Set<byte[]> keys = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().keys("*".getBytes());
        assert keys != null;

        for (byte[] data : keys) {
            sb.append(new String(data, 0, data.length));
        }
        log.info("Keys ->{} ", sb);
    }
}
