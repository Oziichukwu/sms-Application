package com.example.smsapplication.services.sms;

import com.example.smsapplication.dtos.MessageDto;
import com.example.smsapplication.web.exceptions.SmsBusinessException;

public interface SmsService {

    MessageDto sendSms(String jsonObject) throws SmsBusinessException;

    MessageDto receiveSms(String jsonObject) throws SmsBusinessException;

}
