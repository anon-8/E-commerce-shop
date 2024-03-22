package com.anon.ecom.controllers;
import com.anon.ecom.domain.dto.AuthDto;
import com.anon.ecom.domain.dto.RegisterDto;
import com.anon.ecom.domain.responses.AuthResponse;
import com.anon.ecom.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterDto request)   {
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthDto request)   {
        return ResponseEntity.ok(authService.authenticate(request));
    }

}

