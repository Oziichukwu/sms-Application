package com.example.smsapplication.security;

import com.example.smsapplication.data.models.Account;
import com.example.smsapplication.data.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Autowired
    public CustomUserDetailService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException(String.format("Account not found with this username %s", username)));

        return new User(account.getUsername(), account.getAuthId(), new ArrayList<>());
    }

    public Account internalFindUserByUsername(String username){

        return accountRepository.findByUsername(username).orElse(null);
    }

}
