package com.anon.ecom.user.service;

import com.anon.ecom.user.UserRepository;
import com.anon.ecom.user.domain.dto.UserDto;
import com.anon.ecom.user.domain.entity.UserEntity;
import com.anon.ecom.user.exception.UserNotAuthenticatedException;
import com.anon.ecom.user.exception.UserNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public List<UserEntity> findAll() {
        return new ArrayList<>(userRepository.findAll());
    }
    @Override
    public Optional<UserEntity> findById(Long id) {return userRepository.findById(id);
    }
    @Override
    public boolean isExists(Long id) {
        return !userRepository.existsById(id);
    }

    @Override
    public UserEntity partialUpdate(Long id, UserDto userDto) {
        return userRepository.findById(id).map(existingUser -> {
            Optional.ofNullable(userDto.getFirstname()).ifPresent(existingUser::setFirstname);
            Optional.ofNullable(userDto.getLastname()).ifPresent(existingUser::setLastname);
            Optional.ofNullable(userDto.getEmail()).ifPresent(existingUser::setEmail);
            Optional.ofNullable(userDto.getPassword()).ifPresent(existingUser::setPassword);
            Optional.ofNullable(userDto.getRole()).ifPresent(existingUser::setRole);

            Optional.ofNullable(userDto.getDateOfBirth()).ifPresent(existingUser::setDateOfBirth);
            Optional.ofNullable(userDto.getPhoneNumber()).ifPresent(existingUser::setPhoneNumber);
            Optional.ofNullable(userDto.getBankAccountNumber()).ifPresent(existingUser::setBankAccountNumber);
            Optional.ofNullable(userDto.getCountry()).ifPresent(existingUser::setCountry);
            Optional.ofNullable(userDto.getCity()).ifPresent(existingUser::setCity);
            Optional.ofNullable(userDto.getPostCode()).ifPresent(existingUser::setPostCode);
            Optional.ofNullable(userDto.getStreet()).ifPresent(existingUser::setStreet);
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserEntity getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
            throw new UserNotAuthenticatedException("User not authenticated");
        }
        String username = authentication.getName();
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }
}
