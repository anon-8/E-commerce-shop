package itemCopyTest;

import com.anon.ecom.EcomApi;

import com.anon.ecom.itemCopy.ItemCopyRepository;
import com.anon.ecom.itemCopy.domain.ItemCopyEntity;
import com.anon.ecom.itemCopy.services.ItemCopyService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = EcomApi.class)
@TestPropertySource(locations = "classpath:application.properties")
class ItemCopyRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    private ItemCopyRepository itemCopyRepository;

    private Long testItemCopyId;

    @BeforeEach
    void setUp() {
        ItemCopyEntity testItemCopyEntity = ItemCopyEntity.builder()
                .copyKey("AAAAA-BBBB-CCCC-DDDD-EEE")
                .price(BigDecimal.TEN)
                .build();

        ItemCopyEntity savedItemCopyEntity = itemCopyRepository.save(testItemCopyEntity);
        assertNotNull(savedItemCopyEntity);
        assertNotNull(savedItemCopyEntity.getId());

        testItemCopyId = savedItemCopyEntity.getId();
    }

    @AfterEach
    void tearDown() {
        itemCopyRepository.deleteById(testItemCopyId);
    }

    @Test
    void shouldEstablishConnection() {
        assertTrue(postgres.isCreated());
        assertTrue(postgres.isRunning());
    }

    @Test
    void shouldFindItemById() {
        Optional<ItemCopyEntity> foundItemCopy = itemCopyRepository.findById(testItemCopyId);

        assertTrue(foundItemCopy.isPresent());
    }

    @Test
    void shouldFindAllItems() {
        List<ItemCopyEntity> itemCopyList = itemCopyRepository.findAll();
        assertFalse(itemCopyList.isEmpty());
    }

    @Test
    void shouldDeleteItem() {
        itemCopyRepository.deleteById(testItemCopyId);
        Optional<ItemCopyEntity> deletedItemCopy = itemCopyRepository.findById(testItemCopyId);
        assertTrue(deletedItemCopy.isEmpty());
    }
}
