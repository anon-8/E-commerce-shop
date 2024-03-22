package com.anon.ecom.services;

import com.anon.ecom.domain.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserEntity save(UserEntity userEntity);
    List<UserEntity> findAll();
    Optional<UserEntity> findOne(Long id);
    boolean isExists(Long id);
    UserEntity partialUpdate(Long id, UserEntity userEntity);
    void delete(Long id);
    UserEntity getUser();
}
