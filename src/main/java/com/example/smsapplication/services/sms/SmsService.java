package com.example.smsapplication.services.sms;

import com.example.smsapplication.dtos.MessageDto;
import com.example.smsapplication.web.exceptions.SmsBusinessException;

public interface SmsService {

    MessageDto sendSms(MessageDto messageDto) throws SmsBusinessException;

    MessageDto receiveSms(MessageDto messageDto) throws SmsBusinessException;

}
