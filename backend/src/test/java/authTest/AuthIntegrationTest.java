package authTest;

import com.anon.ecom.EcomApi;
import com.anon.ecom.auth.domain.AuthRequest;
import com.anon.ecom.auth.domain.RegisterRequest;
import com.anon.ecom.auth.services.AuthService;
import com.anon.ecom.user.UserRepository;
import com.anon.ecom.user.domain.entity.Role;
import com.anon.ecom.user.domain.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = EcomApi.class)
@TestPropertySource(locations = "classpath:application.properties")
class AuthIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldEstablishConnection() {
        assertTrue(postgres.isCreated());
        assertTrue(postgres.isRunning());
    }

    @Test
    void shouldRegisterUser() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("test")
                .firstname("Test")
                .lastname("Test")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();
        Long testUserId = authService.register(registerRequest).getId();

        UserEntity registeredUser = userRepository.findById(testUserId).get();

        assertEquals(registeredUser.getUsername(), "test");
        assertEquals(registeredUser.getEmail(), "test@example.com");
        assertEquals(registeredUser.getRole(), Role.USER);
    }

    @Test
    void shouldLogin() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("test")
                .firstname("Test")
                .lastname("Test")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();
        authService.register(registerRequest).getId();

        AuthRequest authRequest = AuthRequest.builder()
                .username("test")
                .password("password")
                .build();
        Long testUserId = authService.authenticate(authRequest).getId();

        UserEntity registeredUser = userRepository.findById(testUserId).get();

        assertEquals(registeredUser.getUsername(), "test");
        assertEquals(registeredUser.getEmail(), "test@example.com");
        assertEquals(registeredUser.getRole(), Role.USER);
    }


}
