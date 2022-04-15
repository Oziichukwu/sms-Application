package com.example.smsapplication.service.phoneNumber;

import com.example.smsapplication.data.models.Account;
import com.example.smsapplication.data.models.PhoneNumber;
import com.example.smsapplication.data.repositories.AccountRepository;
import com.example.smsapplication.data.repositories.PhoneNumberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PhoneNumberServiceTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PhoneNumberRepository phoneNumberRepository;

    Account savedAccount;
    PhoneNumber savedPhoneNumber;


    @BeforeEach
    void setUp() {
        phoneNumberRepository.deleteAll();

        Account account = Account.builder()
                .authId("20S0KPNOIM")
                .username("azr1")
                .build();
        savedAccount = accountRepository.save(account);

        PhoneNumber phonenumber = PhoneNumber.builder()
                .number("4924195509198")
                .accountOwner(savedAccount)
                .build();
        savedPhoneNumber = phoneNumberRepository.save(phonenumber);
    }

    @Test
    @DisplayName("Test that phoneNumber can be saved")
    void savePhoneNumber_test() {
        PhoneNumber anotherPhoneNumber = PhoneNumber.builder()
                .number("4924195509196")
                .accountOwner(savedAccount)
                .build();
        PhoneNumber newlySavedPhoneNumber = phoneNumberRepository.save(anotherPhoneNumber);

        assertThat(newlySavedPhoneNumber).isNotNull();
        assertThat(newlySavedPhoneNumber.getAccountOwner().getUsername()).isEqualTo("azr1");
        assertThat(phoneNumberRepository.findAll().size()).isEqualTo(2);
    }


    @Test
    @DisplayName("Test that saved phone number can be removed from database")
    void deleteSavedPhoneNumberFromDatabase() {
        PhoneNumber phoneNumber = PhoneNumber.builder()
                .number("4924195509198")
                .accountOwner(savedAccount)
                .build();
        PhoneNumber newlySavedPhoneNumber = phoneNumberRepository.save(phoneNumber);

        assertThat(phoneNumberRepository.findAll().size()).isEqualTo(2);

        phoneNumberRepository.deleteById(newlySavedPhoneNumber.getId());
        assertThat(phoneNumberRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test that saved phoneNumber can be found by number")
    void findPhoneNumberByNumber() {
        Optional<PhoneNumber> foundPhoneNumber = phoneNumberRepository.findByNumber(savedPhoneNumber.getNumber());
        assert foundPhoneNumber.isPresent();

        assertThat(foundPhoneNumber.get().getId()).isEqualTo(savedPhoneNumber.getId());
        assertThat(foundPhoneNumber.get().getAccountOwner().getUsername()).isEqualTo("azr1");
        assertThat(foundPhoneNumber.get().getId()).isEqualTo(savedPhoneNumber.getId());
    }


    @Test
    @DisplayName("Test that saved phoneNumber can be found by id")
    void findPhoneNumberById() {
        Optional<PhoneNumber> foundPhoneNumber = phoneNumberRepository.findById(savedPhoneNumber.getId());
        assert foundPhoneNumber.isPresent();

        assertThat(foundPhoneNumber.get().getId()).isEqualTo(savedPhoneNumber.getId());
        assertThat(foundPhoneNumber.get().getAccountOwner().getUsername()).isEqualTo("azr1");
        assertThat(foundPhoneNumber.get().getId()).isEqualTo(savedPhoneNumber.getId());
    }


}
