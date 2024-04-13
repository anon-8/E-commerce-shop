package com.anon.ecom.user;

import com.anon.ecom.auth.domain.RegisterRequest;
import com.anon.ecom.auth.services.AuthService;
import com.anon.ecom.user.domain.entity.Role;
import com.anon.ecom.user.domain.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/ecom",
        "spring.datasource.username=postgres",
        "spring.datasource.password=anon123"
})
class UserRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    @MockBean
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    private Long testUserId;

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @BeforeEach
    void setUp() {
        RegisterRequest testUserRequest = RegisterRequest.builder()
                .username("test")
                .firstname("Test")
                .lastname("Test")
                .email("test@example.com")
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
    void shouldFindUserByUsername() {
        Optional<UserEntity> userOptional = userRepository.findByUsername("test");
        assertThat(userOptional).isPresent();
        UserEntity user = userOptional.get();
        assertThat(user.getUsername()).isEqualTo("test");
        assertThat(user.getFirstname()).isEqualTo("Test");
        assertThat(user.getLastname()).isEqualTo("Test");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }


    @Test
    void findByUsernameOrEmail() {
        // Test findByUsernameOrEmail method
    }
}
