package com.example.smsapplication.services.account;

import com.example.smsapplication.data.models.Account;
import com.example.smsapplication.data.repositories.AccountRepository;
import com.example.smsapplication.web.exceptions.SmsBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;

    @Autowired
    AccountServiceImpl(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }


    @Override
    public Account findAccountById(Integer accountId) throws SmsBusinessException {

        if (accountId == null){
            throw new IllegalArgumentException("Account Id cannot be empty");
        }

        return accountRepository.findById(accountId).orElseThrow(()->
                new SmsBusinessException("Account with id :" + accountId + " does not exist"));
    }
}
