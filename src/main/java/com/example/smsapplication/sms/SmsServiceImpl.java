package com.example.smsapplication.sms;


import com.example.smsapplication.data.models.PhoneNumber;
import com.example.smsapplication.dtos.MessageDto;
import com.example.smsapplication.services.phoneNumber.PhoneNumberService;
import com.example.smsapplication.services.redis.RedisRateLimiter;
import com.example.smsapplication.web.exceptions.MessageApplicationException;
import com.example.smsapplication.web.exceptions.SmsBusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService{

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisRateLimiter rateLimiter;
    private final PhoneNumberService phoneNumberService;

    private final ObjectMapper objectMapper;
    private final Gson gson = new Gson();


    @Autowired
    public SmsServiceImpl(RedisTemplate<String, String> redisTemplate, RedisRateLimiter rateLimiter,
                          PhoneNumberService phoneNumberService, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.rateLimiter = rateLimiter;
        this.phoneNumberService = phoneNumberService;
        this.objectMapper = objectMapper;
    }

    @Override
    public MessageDto sendSms(MessageDto messageDto) throws SmsBusinessException {

        MessageDto messageRequest = transformJsonInput(messageDto);

        checkValidityOfInput(messageRequest);

        return validateAmountOfApiCalls(messageRequest);
    }

    private MessageDto validateAmountOfApiCalls(MessageDto messageRequest) throws MessageApplicationException {

        boolean isAllowed = rateLimiter.isAllowed(messageRequest.getSmsSender());

        if (isAllowed){
            checkCache(messageRequest);
            return objectMapper.convertValue(messageRequest, MessageDto.class);
        }else throw new MessageApplicationException("Maximum API call limit exceeded");

    }

    private void checkCache(MessageDto messageRequest) throws MessageApplicationException {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(messageRequest.getSmsSender()))){
            throw new MessageApplicationException("Message from " + messageRequest.getSmsSender() + " to " +
                    messageRequest.getSmsReceiver() + " has been blocked by STOP request");
        }
    }

    private void checkValidityOfInput(MessageDto messageRequest) throws SmsBusinessException {
        if (messageRequest.getSmsSender().isEmpty()) throw new MessageApplicationException("The Sender number cannot be empty");
        if (messageRequest.getSmsReceiver().isEmpty()) throw new MessageApplicationException("The Receiver number cannot be empty");
        if (messageRequest.getSmsBody().isEmpty()) throw new MessageApplicationException("The Message body cannot be empty");

        phoneNumberService.findPhoneNumberByNumber(messageRequest.getSmsReceiver());
        phoneNumberService.findPhoneNumberByNumber(messageRequest.getSmsSender());
    }


    @Override
    public MessageDto receiveSms(MessageDto request) throws SmsBusinessException {

        MessageDto messageDto = transformJsonInput(request);
        checkValidityOfInput(messageDto);

        MessageDto messageResponse = objectMapper.convertValue(messageDto, MessageDto.class);

        checkForStopWordInSms(messageResponse);
        return messageResponse;
    }

    private void checkForStopWordInSms(MessageDto messageResponse) throws SmsBusinessException {
        PhoneNumber foundSender = phoneNumberService.findPhoneNumberByNumber(messageResponse.getSmsSender());
        Set<String> savedWords = Set.of(messageResponse.getSmsBody().split("[ !@#$%^&*()_+}{\":?><,./;'=]"));

        if (savedWords.contains("STOP") || savedWords.contains("stop")){
            redisTemplate.opsForHash().put(messageResponse.getSmsSender(),foundSender.getId(),messageResponse.getSmsReceiver());
            redisTemplate.expire(messageResponse.getSmsSender(), 4 , TimeUnit.HOURS);
        }
    }


    private MessageDto transformJsonInput(MessageDto messageDto) throws MessageApplicationException {

        if(messageDto == null){
            throw new MessageApplicationException("Json cannot be empty");
        }
        return gson.fromJson(String.valueOf(messageDto), MessageDto.class);
    }
}
