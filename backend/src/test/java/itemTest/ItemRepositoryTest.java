package itemTest;

import com.anon.ecom.EcomApi;
import com.anon.ecom.item.ItemRepository;
import com.anon.ecom.item.domain.ItemEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = EcomApi.class)
@TestPropertySource(locations = "classpath:application.properties")
class ItemRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    ItemRepository itemRepository;

    private Long testItemId;

    @BeforeEach
    void setUp() {
        ItemEntity testItemEntity = ItemEntity.builder()
                .title("test title")
                .developer("test developer")
                .platform("test platform")
                .tags(new ArrayList<>())
                .build();

        ItemEntity savedItemEntity = itemRepository.save(testItemEntity);
        assertNotNull(savedItemEntity);
        assertNotNull(savedItemEntity.getId());

        testItemId = savedItemEntity.getId();
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteById(testItemId);
    }

    @Test
    void shouldEstablishConnection() {
        assertTrue(postgres.isCreated());
        assertTrue(postgres.isRunning());
    }

    @Test
    void shouldFindItemById() {
        Optional<ItemEntity> foundItem = itemRepository.findById(testItemId);

        assertTrue(foundItem.isPresent());
    }

    @Test
    void shouldFindAllItems() {
        List<ItemEntity> itemList = itemRepository.findAll();
        assertFalse(itemList.isEmpty());
    }

    @Test
    void shouldDeleteItem() {
        itemRepository.deleteById(testItemId);
        Optional<ItemEntity> deletedItem = itemRepository.findById(testItemId);
        assertTrue(deletedItem.isEmpty());
    }
}
