package userTest;

import com.anon.ecom.config.Mapper;
import com.anon.ecom.user.UserController;
import com.anon.ecom.user.domain.dto.UserDto;
import com.anon.ecom.user.domain.entity.UserEntity;
import com.anon.ecom.user.exceptions.UserNotFoundException;
import com.anon.ecom.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Mapper<UserEntity, UserDto> userMapper;

    @InjectMocks
    private UserController userController;

    @Test
    public void shouldListUsers() {
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        when(userService.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(userMapper.mapTo(user1)).thenReturn(new UserDto());
        when(userMapper.mapTo(user2)).thenReturn(new UserDto());

        List<UserDto> result = userController.listUsers();

        assertEquals(2, result.size());
    }

    @Test
    public void shouldGetUser() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.mapTo(user)).thenReturn(new UserDto());

        ResponseEntity<UserDto> responseEntity = userController.getUser(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void shouldGetUser_UserNotFound() {
        Long userId = 1L;
        when(userService.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userController.getUser(userId));
    }

    @Test
    public void shouldPartialUpdateUser() {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        UserEntity userEntity = new UserEntity();
        when(userService.partialUpdate(userId, userDto)).thenReturn(userEntity);
        when(userMapper.mapTo(userEntity)).thenReturn(new UserDto());

        ResponseEntity<UserDto> responseEntity = userController.partialUpdateUser(userId, userDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void shouldUserPartialUpdate() {
        UserDto userDto = new UserDto();
        UserEntity userEntity = new UserEntity();
        when(userService.getUser()).thenReturn(userEntity);
        when(userService.partialUpdate(userEntity.getId(), userDto)).thenReturn(userEntity);
        when(userMapper.mapTo(userEntity)).thenReturn(new UserDto());

        ResponseEntity<UserDto> responseEntity = userController.userPartialUpdate(userDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void shouldDeleteUser() {
        Long userId = 1L;

        var responseEntity = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(userService, times(1)).delete(userId);
    }
}
