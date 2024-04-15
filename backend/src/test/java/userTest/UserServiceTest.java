package userTest;

import com.anon.ecom.user.UserRepository;
import com.anon.ecom.user.domain.dto.UserDto;
import com.anon.ecom.user.domain.entity.UserEntity;
import com.anon.ecom.user.exceptions.UserNotFoundException;
import com.anon.ecom.user.exceptions.UserNotAuthenticatedException;
import com.anon.ecom.user.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldFindUserById() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> foundUser = userService.findById(userId);

        assertTrue(foundUser.isPresent());
        assertEquals(userEntity, foundUser.get());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldExistUserById() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        boolean exists = userService.isExists(userId);

        assertTrue(exists);
        verify(userRepository, times(1)).existsById(userId);
    }

    @Test
    void shouldPartialUpdateUser() {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setFirstname("John");
        UserEntity existingUser = new UserEntity();
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        UserEntity updatedUser = userService.partialUpdate(userId, userDto);

        assertNotNull(updatedUser);
        assertEquals("John", updatedUser.getFirstname());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void shouldDeleteUserById() {
        Long userId = 1L;

        userService.delete(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void shouldGetAuthenticatedUser() {
        String username = "testuser";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "password");
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        UserEntity userEntity = new UserEntity();
        when(userRepository.findByUsernameOrEmail(username, username)).thenReturn(Optional.of(userEntity));

        UserEntity foundUser = userService.getUser();

        assertNotNull(foundUser);
        assertEquals(userEntity, foundUser);
        verify(userRepository, times(1)).findByUsernameOrEmail(username, username);
    }

    @Test
    void shouldThrowExceptionForUnauthenticatedUser() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(UserNotAuthenticatedException.class, () -> userService.getUser());
    }

    @Test
    void shouldThrowExceptionForUserNotFound() {
        String username = "testuser";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "password");
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findByUsernameOrEmail(username, username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser());
    }
}
