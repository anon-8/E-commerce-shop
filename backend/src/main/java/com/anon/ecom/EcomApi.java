package com.anon.ecom;

import com.anon.ecom.auth.domain.AuthRequest;
import com.anon.ecom.auth.domain.RegisterRequest;
import com.anon.ecom.user.domain.entity.Role;
import com.anon.ecom.auth.services.AuthService;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.anon.ecom.*"})
@Log
public class EcomApi {

    final AuthService authService;

    public EcomApi(AuthService authService) {
        this.authService = authService;
    }

    public static void main(String[] args) {
        SpringApplication.run(EcomApi.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            registerOrAuthenticate("admin", "Admin", "Admin", "admin@mail.com", "password", Role.ADMIN);
            registerOrAuthenticate("manager", "Manager", "Manager", "manager@mail.com", "password", Role.MANAGER);
        };
    }

    private void registerOrAuthenticate(String username, String firstname, String lastname, String email, String password, Role role) {
        if (authService.existsByUsername(username)) {
            RegisterRequest request = RegisterRequest.builder()
                    .username(username)
                    .firstname(firstname)
                    .lastname(lastname)
                    .email(email)
                    .password(password)
                    .role(role)
                    .build();
            log.info(role.name() + " token: " + authService.register(request).getToken());
        } else {
            AuthRequest request = AuthRequest.builder()
                    .username(username)
                    .password(password)
                    .build();
            log.info(role.name() + " token: " + authService.authenticate(request).getToken());
        }
    }
}
