package com.anon.ecom.auth.services;

import com.anon.ecom.auth.domain.AuthDto;
import com.anon.ecom.auth.domain.AuthResponse;
import com.anon.ecom.user.domain.dto.RegisterDto;

public interface   AuthService {
    AuthResponse register(RegisterDto request);

    AuthResponse authenticate(AuthDto request);

    boolean existsByUsername(String username);

}
