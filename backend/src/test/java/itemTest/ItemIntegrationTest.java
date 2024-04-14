package itemTest;

import com.anon.ecom.EcomApi;
import com.anon.ecom.item.ItemRepository;
import com.anon.ecom.item.domain.ItemEntity;
import com.anon.ecom.item.services.ItemService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = EcomApi.class)
class ItemIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    private Long testItemId;

    @BeforeEach
    void setUp() {
        ItemEntity testItemEntity = ItemEntity.builder()
                .title("test title")
                .developer("test developer")
                .platform("test platform")
                .tags(new ArrayList<>())
                .build();

        ItemEntity savedItemEntity = itemService.save(testItemEntity);
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
        Optional<ItemEntity> foundItem = itemService.findOne(testItemId);

        assertTrue(foundItem.isPresent());
    }

    @Test
    void shouldFindAllItems() {
        List<ItemEntity> itemList = itemService.findAll();
        assertFalse(itemList.isEmpty());
    }

    @Test
    void shouldUpdateItem() {
        ItemEntity updatedItem = itemService.partialUpdate(testItemId, ItemEntity.builder().title("Updated Title").build());
        assertNotNull(updatedItem);
        assertEquals("Updated Title", updatedItem.getTitle());
    }

    @Test
    void shouldDeleteItem() {
        itemService.delete(testItemId);
        Optional<ItemEntity> deletedItem = itemService.findOne(testItemId);
        assertTrue(deletedItem.isEmpty());
    }
}
