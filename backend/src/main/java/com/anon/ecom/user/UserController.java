package com.anon.ecom.user;

import com.anon.ecom.config.Mapper;
import com.anon.ecom.user.domain.dto.UserDto;
import com.anon.ecom.user.domain.entity.UserEntity;
import com.anon.ecom.user.exceptions.UserNotFoundException;
import com.anon.ecom.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final Mapper<UserEntity, UserDto> userMapper;

    @Autowired
    public UserController(UserService userService, Mapper<UserEntity, UserDto> userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping(path = "/management/users")
    public List<UserDto> listUsers() {
        List<UserEntity> users = userService.findAll();
        return users.stream()
                .map(userMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/management/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        Optional<UserEntity> foundUser = userService.findById(id);
        if (foundUser.isPresent()) {
            UserDto userDto = userMapper.mapTo(foundUser.get());
            return ResponseEntity.ok(userDto);
        } else {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    }


    @PatchMapping(path = "/admin/partial-update-user/{id}")
    public ResponseEntity<UserDto> partialUpdateUser(
            @PathVariable("id") Long id,
            @RequestBody UserDto userDto
    ) {
        UserEntity updatedUserEntity = userService.partialUpdate(id, userDto);
        return ResponseEntity.ok(userMapper.mapTo(updatedUserEntity));
    }

    @PatchMapping(path = "/partial-update-user")
    public ResponseEntity<UserDto> userPartialUpdate(
            @RequestBody UserDto userDto
    ) {
        UserEntity user = userService.getUser();
        UserEntity updatedUserEntity = userService.partialUpdate(user.getId(), userDto);
        return ResponseEntity.ok(userMapper.mapTo(updatedUserEntity));
    }


    @DeleteMapping(path = "/admin/user/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
