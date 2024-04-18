package com.anon.ecom.auth.services;

import com.anon.ecom.auth.domain.AuthRequest;
import com.anon.ecom.auth.domain.AuthResponse;
import com.anon.ecom.auth.domain.RegisterRequest;
import com.anon.ecom.user.domain.entity.Role;
import com.anon.ecom.user.domain.entity.UserEntity;
import com.anon.ecom.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.anon.ecom.user.domain.entity.Role.USER;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtServiceImpl;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtServiceImpl jwtServiceImpl, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtServiceImpl = jwtServiceImpl;
        this.authenticationManager = authenticationManager;
    }
    @Override
    public AuthResponse register(RegisterRequest request) {

        Role role = request.getRole() != null ? request.getRole() : USER;

        var user = UserEntity.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
        userRepository.save(user);

        var jwtToken = jwtServiceImpl.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .id(user.getId())
                .build();
    }
    @Override
    public AuthResponse authenticate(AuthRequest request) {

        String username = request.getUsername();
        var user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtServiceImpl.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .id(user.getId())
                .build();
    }

    @Override
    public boolean existsByUsername(String username) {

        Optional<UserEntity> existingUser = userRepository.findByUsername(username);

        return existingUser.isEmpty();
    }

}
