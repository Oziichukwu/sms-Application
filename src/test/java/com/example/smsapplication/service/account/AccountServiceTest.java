package com.example.smsapplication.service.account;

import com.example.smsapplication.data.models.Account;
import com.example.smsapplication.data.models.PhoneNumber;
import com.example.smsapplication.data.repositories.AccountRepository;
import com.example.smsapplication.data.repositories.PhoneNumberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class AccountServiceTest {

    private final AccountRepository accountRepository;

    private final PhoneNumberRepository phoneNumberRepository;

    @Autowired
    public AccountServiceTest(AccountRepository accountRepository,
                              PhoneNumberRepository phoneNumberRepository) {
        this.accountRepository = accountRepository;
        this.phoneNumberRepository = phoneNumberRepository;
    }

    @BeforeEach
    void setUp(){
        accountRepository.deleteAll();
        Account account = Account.builder()
                .username("azr1")
                .authId("20S0KPNOIM")
                .build();
        Account savedAccount = accountRepository.save(account);

        PhoneNumber phonenumber = PhoneNumber.builder()
                .number("4924195509198")
                .accountOwner(savedAccount)
                .build();
       PhoneNumber savedPhoneNumber = phoneNumberRepository.save(phonenumber);
    }

    @Test
    @DisplayName("Test that account can be saved test")
    void saveAccountTest(){
        Account account = Account.builder()
                .username("azr1")
                .authId("20S0KPNOIM")
                .build();

        Account savedAccount = accountRepository.save(account);
        assertThat(savedAccount.getId()).isNotNull();
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getUsername()).isEqualTo("azr1");
    }

    @Test
    @DisplayName("Test that account can be retrieved from the database test")
    void retrieveAccountFromDatabaseTest(){

        Account foundAccount = accountRepository.findById(1).orElse(null);
        assertThat(foundAccount).isNotNull();
        assertThat(foundAccount.getId()).isEqualTo(1);
        assertThat(foundAccount.getUsername()).isEqualTo("20S0KPNOIM");
        assertThat(foundAccount.getAuthId()).isEqualTo("azr1");

    }

}
