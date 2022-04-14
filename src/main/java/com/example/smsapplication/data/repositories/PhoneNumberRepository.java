package com.example.smsapplication.data.repositories;

import com.example.smsapplication.data.models.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Integer> {

    Optional<PhoneNumber>findByNumber(String number);
}
