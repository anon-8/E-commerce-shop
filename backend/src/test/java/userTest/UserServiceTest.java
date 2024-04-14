package userTest;

import com.anon.ecom.EcomApi;
import com.anon.ecom.user.domain.dto.UserDto;
import com.anon.ecom.user.domain.entity.UserEntity;
import com.anon.ecom.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = EcomApi.class)
public class UserServiceTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    UserService userService;

    @Test
    void shouldPartialUpdate() {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstname("John");
        userEntity.setLastname("Doe");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setPassword("password");
        userEntity.setCity("test");
        userEntity.setCountry("test");

        Long existingUserId = userService.save(userEntity).getId();

        UserDto userDto = new UserDto();
        userDto.setFirstname("Jack");
        userDto.setLastname("Black");
        userDto.setEmail("(jack).black@example.com");
        userDto.setPassword("password1");
        userDto.setCity("test1");
        userDto.setCountry("test1");

        userService.partialUpdate(existingUserId, userDto);

        Optional<UserEntity> updatedUser = userService.findById(existingUserId);
        assertEquals(userDto.getFirstname(), updatedUser.get().getFirstname());
        assertEquals(userDto.getLastname(), updatedUser.get().getLastname());
        assertEquals(userDto.getEmail(), updatedUser.get().getEmail());
        assertEquals(userDto.getPassword(), updatedUser.get().getPassword());
        assertEquals(userDto.getCountry(), updatedUser.get().getCountry());
        assertEquals(userDto.getCity(), updatedUser.get().getCity());
    }



}
