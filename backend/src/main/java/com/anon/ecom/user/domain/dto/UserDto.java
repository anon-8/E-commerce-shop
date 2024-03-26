package com.anon.ecom.user.domain.dto;

import com.anon.ecom.user.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String username;

    private String password;

    private String email;

    private String firstname;

    private String lastname;

    private LocalDate dateOfBirth;

    private int age;

    private String bankAccountNumber;

    private String country;

    private String city;

    private String postCode;

    private String street;

    private String phoneNumber;

    private Role role;
}