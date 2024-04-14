package com.anon.ecom.user;

import com.anon.ecom.config.Mapper;
import com.anon.ecom.exception.ApiRequestException;
import com.anon.ecom.user.domain.dto.UserDto;
import com.anon.ecom.user.domain.entity.UserEntity;
import com.anon.ecom.user.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/management")
public class UserController {
    private final UserService userService;
    private final Mapper<UserEntity, UserDto> userMapper;

    public UserController(UserService userService, Mapper<UserEntity, UserDto> userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping(path = "/users")
    public List<UserDto> listUsers() {
        List<UserEntity> users = userService.findAll();
        return users.stream()
                .map(userMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        Optional<UserEntity> foundUser = userService.findById(id);
        if (foundUser.isPresent()) {
            UserDto userDto = userMapper.mapTo(foundUser.get());
            return ResponseEntity.ok(userDto);
        } else {
            throw new ApiRequestException("User not found with ID: " + id);
        }
    }

    @PutMapping(path = "/user/{id}")
    public ResponseEntity<UserDto> fullUpdateUser(
            @PathVariable("id") Long id,
            @RequestBody UserDto userDto) {
        userDto.setId(id);
        UserEntity userEntity = userMapper.mapFrom(userDto);
        UserEntity savedUserEntity = userService.save(userEntity);
        return ResponseEntity.ok(userMapper.mapTo(savedUserEntity));
    }

    @PatchMapping(path = "/user/{id}")
    public ResponseEntity<UserDto> partialUpdate(
            @PathVariable("id") Long id,
            @RequestBody UserDto userDto
    ) {
        UserEntity userEntity = userMapper.mapFrom(userDto);
        UserEntity updatedUser = userService.partialUpdate(id, userDto);
        return ResponseEntity.ok(userMapper.mapTo(updatedUser));
    }

    @DeleteMapping(path = "/user/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
