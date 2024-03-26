package com.anon.ecom.user.domain.dto;

import com.anon.ecom.user.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDto {
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
    private Role role;

}
