package com.example.smsapplication.services.account;

import com.example.smsapplication.data.models.Account;
import com.example.smsapplication.web.exceptions.SmsBusinessException;

public interface AccountService {

    Account findAccountById(Integer accountId) throws SmsBusinessException;
}
