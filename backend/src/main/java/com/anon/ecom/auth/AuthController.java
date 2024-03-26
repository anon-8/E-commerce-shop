package com.anon.ecom.auth;
import com.anon.ecom.auth.domain.AuthRequest;
import com.anon.ecom.auth.domain.AuthResponse;
import com.anon.ecom.auth.services.AuthService;
import com.anon.ecom.auth.domain.RegisterRequest;
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
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request)   {
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request)   {
        return ResponseEntity.ok(authService.authenticate(request));
    }

}

