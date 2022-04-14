package com.example.smsapplication.services.phoneNumber;

import com.example.smsapplication.data.models.PhoneNumber;
import com.example.smsapplication.web.exceptions.SmsBusinessException;

public interface PhoneNumberService {

    PhoneNumber findPhoneNumberByNumber(String number) throws SmsBusinessException;
}
