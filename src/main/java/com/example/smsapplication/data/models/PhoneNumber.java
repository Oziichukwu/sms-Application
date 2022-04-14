package com.example.smsapplication.data.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumber implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(nullable = false)
    private Integer id;

    @Column(length = 16)
    private String number;

    @ManyToOne
    private Account accountOwner;
}
