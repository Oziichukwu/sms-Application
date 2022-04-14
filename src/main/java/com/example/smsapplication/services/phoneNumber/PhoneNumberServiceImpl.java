package com.example.smsapplication.services.phoneNumber;

import com.example.smsapplication.data.models.PhoneNumber;
import com.example.smsapplication.data.repositories.PhoneNumberRepository;
import com.example.smsapplication.web.exceptions.SmsBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService{

    private final PhoneNumberRepository phoneNumberRepository;

    @Autowired
    public PhoneNumberServiceImpl(PhoneNumberRepository phoneNumberRepository) {
        this.phoneNumberRepository = phoneNumberRepository;
    }


    @Override
    public PhoneNumber findPhoneNumberByNumber(String number) throws SmsBusinessException {

        if( number == null){
            throw new IllegalArgumentException("Number cannot be empty");
        }

        return phoneNumberRepository.findByNumber(number).orElseThrow(()->
                new SmsBusinessException("PhoneNumber " + number + " does not exist"));
    }
}
