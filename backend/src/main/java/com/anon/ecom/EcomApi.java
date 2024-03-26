package com.anon.ecom;

import com.anon.ecom.auth.domain.AuthDto;
import com.anon.ecom.user.domain.dto.RegisterDto;
import com.anon.ecom.user.domain.entity.Role;
import com.anon.ecom.auth.services.AuthService;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Log
public class EcomApi {

    public static void main(String[] args) {
        SpringApplication.run(EcomApi.class, args); }

    @Bean
    public CommandLineRunner commandLineRunner(
            AuthService service
    ) {
        return args -> {
            if (service.existsByUsername("admin")) {
                RegisterDto adminRequest = RegisterDto.builder()
                        .username("admin")
                        .firstname("Admin")
                        .lastname("Admin")
                        .email("admin@mail.com")
                        .password("password")
                        .role(Role.ADMIN)
                        .build();
                System.out.println("Admin token: " + service.register(adminRequest).getToken());
            } else {
                AuthDto adminReq = AuthDto.builder()
                        .username("admin")
                        .password("password")
                        .build();
                System.out.println("Admin token: " + service.authenticate(adminReq).getToken());
            }

            if (service.existsByUsername("manager")) {
                RegisterDto managerRequest = RegisterDto.builder()
                        .username("manager")
                        .firstname("Manager")
                        .lastname("Manager")
                        .email("manager@mail.com")
                        .password("password")
                        .role(Role.MANAGER)
                        .build();
                System.out.println("Manager token: " + service.register(managerRequest).getToken());
            } else {
                AuthDto managerReq = AuthDto.builder()
                        .username("manager")
                        .password("password")
                        .build();
                System.out.println("Manager token: " + service.authenticate(managerReq).getToken());
            }
        };
    }

}
