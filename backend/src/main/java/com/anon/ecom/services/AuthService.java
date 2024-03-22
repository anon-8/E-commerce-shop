package com.anon.ecom.services;

import com.anon.ecom.domain.dto.AuthDto;
import com.anon.ecom.domain.dto.RegisterDto;
import com.anon.ecom.domain.responses.AuthResponse;

public interface   AuthService {
    AuthResponse register(RegisterDto request);

    AuthResponse authenticate(AuthDto request);

    boolean existsByUsername(String username);

}
