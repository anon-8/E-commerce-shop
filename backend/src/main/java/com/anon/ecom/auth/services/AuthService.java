package com.anon.ecom.auth.services;

import com.anon.ecom.auth.domain.AuthRequest;
import com.anon.ecom.auth.domain.AuthResponse;
import com.anon.ecom.auth.domain.RegisterRequest;

public interface   AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse authenticate(AuthRequest request);

    boolean existsByUsername(String username);

}
