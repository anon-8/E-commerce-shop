package orderTest;

import com.anon.ecom.EcomApi;
import com.anon.ecom.order.OrderRepository;
import com.anon.ecom.order.domain.OrderEntity;
import org.junit.jupiter.api.AfterEach;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = EcomApi.class)
@TestPropertySource(locations = "classpath:application.properties")
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @AfterEach
    void afterEach() {
        orderRepository.deleteAll();
    }

    @Test
    void shouldEstablishConnection() {
        assertTrue(postgres.isCreated());
        assertTrue(postgres.isRunning());
    }

    @Test
    void shouldSaveOrder() {
        OrderEntity orderEntity = createOrderEntity("pending");

        OrderEntity savedOrder = orderRepository.save(orderEntity);

        assertOrderFields(savedOrder, "pending");
    }

    @Test
    void shouldUpdateOrder() {
        OrderEntity orderEntity = createOrderEntity("pending");
        OrderEntity savedOrder = orderRepository.save(orderEntity);

        savedOrder.setStatus("sold");
        OrderEntity updatedOrder = orderRepository.save(savedOrder);

        assertOrderFields(updatedOrder, "sold");
    }

    @Test
    void shouldDeleteOrder() {
        OrderEntity orderEntity = createOrderEntity("pending");
        OrderEntity savedOrder = orderRepository.save(orderEntity);

        orderRepository.deleteById(savedOrder.getId());

        assertFalse(orderRepository.existsById(savedOrder.getId()));
    }

    @Test
    void shouldFindOrder() {
        OrderEntity orderEntity = createOrderEntity("pending");
        OrderEntity savedOrder = orderRepository.save(orderEntity);

        Optional<OrderEntity> foundOrderOptional = orderRepository.findById(savedOrder.getId());

        assertTrue(foundOrderOptional.isPresent());
        assertOrderFields(foundOrderOptional.get(), "pending");
    }

    private OrderEntity createOrderEntity(String status) {
        return OrderEntity.builder()
                .status(status)
                .notifyUrl("https://ecom.com/notify")
                .customerIp("127.0.0.1")
                .description("Digital Key Marketplace")
                .currencyCode("PLN")
                .build();
    }

    private void assertOrderFields(OrderEntity order, String status) {
        assertNotNull(order.getId());
        assertEquals(status, order.getStatus());
        assertEquals("https://ecom.com/notify", order.getNotifyUrl());
        assertEquals("127.0.0.1", order.getCustomerIp());
        assertEquals("Digital Key Marketplace", order.getDescription());
        assertEquals("PLN", order.getCurrencyCode());
    }
}
