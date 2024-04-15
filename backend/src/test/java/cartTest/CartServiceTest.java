package cartTest;

import com.anon.ecom.cart.CartRepository;
import com.anon.ecom.cart.domain.CartItemEntity;
import com.anon.ecom.cart.services.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void shouldFindAllCartItems() {
        List<CartItemEntity> cartItems = new ArrayList<>();
        when(cartRepository.findAll()).thenReturn(cartItems);

        List<CartItemEntity> foundCartItems = cartService.findAll();

        assertEquals(cartItems, foundCartItems);
        verify(cartRepository, times(1)).findAll();
    }

    @Test
    void shouldFindCartItemById() {
        Long cartItemId = 1L;
        CartItemEntity cartItem = new CartItemEntity();
        when(cartRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        Optional<CartItemEntity> foundCartItem = cartService.findOne(cartItemId);

        assertTrue(foundCartItem.isPresent());
        assertEquals(cartItem, foundCartItem.get());
        verify(cartRepository, times(1)).findById(cartItemId);
    }

}
