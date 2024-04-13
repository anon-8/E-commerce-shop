package userTest;

import com.anon.ecom.EcomApi;
import com.anon.ecom.auth.domain.RegisterRequest;
import com.anon.ecom.auth.services.AuthService;
import com.anon.ecom.user.UserRepository;
import com.anon.ecom.user.domain.dto.UserDto;
import com.anon.ecom.user.domain.entity.Role;
import com.anon.ecom.user.domain.entity.UserEntity;
import com.anon.ecom.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = EcomApi.class)
class UserIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    private Long testUserId;

    private void assertUserDetails(UserEntity user) {
        assertEquals("test", user.getUsername());
        assertEquals("Test", user.getFirstname());
        assertEquals("Test", user.getLastname());
        assertEquals("test@example.com", user.getEmail());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    void shouldEstablishConnection() {
        assertTrue(postgres.isCreated());
        assertTrue(postgres.isRunning());
    }

    @BeforeEach
    void setUp() {
        String testUserUsername = "test";
        String testUserEmail = "test@example.com";
        RegisterRequest testUserRequest = RegisterRequest.builder()
                .username(testUserUsername)
                .firstname("Test")
                .lastname("Test")
                .email(testUserEmail)
                .password("password")
                .role(Role.USER)
                .build();
        testUserId = authService.register(testUserRequest).getId();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteById(testUserId);
    }

    @Test
    void shouldFindUserById() {
        Optional<UserEntity> optionalUser = userRepository.findById(testUserId);

        assertTrue(optionalUser.isPresent());
        assertEquals("test", optionalUser.get().getUsername());
    }

    @Test
    void shouldFindUserByUsername() {
        Optional<UserEntity> userOptional = userRepository.findByUsername("test");

        assertTrue((userOptional).isPresent());
        UserEntity user = userOptional.get();
        assertUserDetails(user);
    }

    @Test
    void shouldFindUserByUsernameOrEmail() {
        Optional<UserEntity> userOptional = userRepository.findByUsernameOrEmail("test", "test@example.com");
        assertTrue((userOptional).isPresent());
        UserEntity user = userOptional.get();

        assertUserDetails(user);
    }

    @Test
    void shouldPartialUpdate() {
        UserDto userDto = new UserDto();
        userDto.setFirstname("John");
        userDto.setLastname("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password");
        userDto.setCity("test");
        userDto.setCountry("test");

        userService.partialUpdate(testUserId, userDto);

        Optional<UserEntity> updatedUser = userRepository.findById(testUserId);
        assertEquals(userDto.getFirstname(), updatedUser.get().getFirstname());
        assertEquals(userDto.getLastname(), updatedUser.get().getLastname());
        assertEquals(userDto.getEmail(), updatedUser.get().getEmail());
        assertEquals(userDto.getPassword(), updatedUser.get().getPassword());
        assertEquals(userDto.getCountry(), updatedUser.get().getCountry());
        assertEquals(userDto.getCity(), updatedUser.get().getCity());
    }

    @Test
    void shouldDeleteUser() {
        Optional<UserEntity> userOptional = userRepository.findByUsername("test");
        assertTrue(userOptional.isPresent());
        UserEntity user = userOptional.get();

        userRepository.deleteById(user.getId());

        Optional<UserEntity> deletedUserOptional = userRepository.findByUsername("test");
        assertFalse(deletedUserOptional.isPresent());
    }

    @Test
    void shouldNotFindNonexistentUserByUsername() {
        Optional<UserEntity> userOptional = userRepository.findByUsername("nonexistent");

        assertFalse(userOptional.isPresent());
    }

    @Test
    void shouldNotFindNonexistentUserByUsernameOrEmail() {
        Optional<UserEntity> userOptional = userRepository.findByUsernameOrEmail("nonexistent", "nonexistent@example.com");

        assertFalse(userOptional.isPresent());
    }
}
