package itemTest;

import com.anon.ecom.EcomApi;
import com.anon.ecom.item.ItemRepository;
import com.anon.ecom.item.domain.ItemEntity;
import com.anon.ecom.item.service.ItemServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = EcomApi.class)
class ItemUnitTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemServiceImpl itemService;

    private Long testItemId;

    @BeforeEach
    void setUp() {
        ItemEntity testItemEntity = ItemEntity.builder()
                .title("test title")
                .developer("test developer")
                .platform("test platform")
                .tags(new ArrayList<>())
                .build();

        when(itemRepository.save(testItemEntity)).thenReturn(testItemEntity);

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
        // Mocking the behavior of itemRepository.findById()
        when(itemRepository.findById(testItemId)).thenReturn(Optional.of(new ItemEntity()));

        Optional<ItemEntity> foundItem = itemService.findOne(testItemId);
        assertTrue(foundItem.isPresent());
    }

    // Add more test cases as needed
}
