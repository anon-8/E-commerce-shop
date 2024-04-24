package orderTest;

import com.anon.ecom.order.OrderController;
import com.anon.ecom.order.domain.OrderDto;
import com.anon.ecom.order.payu.PayU;
import com.anon.ecom.order.services.OrderService;
import com.anon.ecom.order.payu.domain.OrderCreateResponse;
import com.anon.ecom.order.payu.domain.ReservationDetails;
import com.anon.ecom.order.payu.domain.PaymentData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private PayU payU;

    @InjectMocks
    private OrderController orderController;

    @Test
    public void testPlaceOrderFromCart() {
        OrderDto orderDto = new OrderDto();
        OrderCreateResponse orderCreateResponse = new OrderCreateResponse();
        orderCreateResponse.setOrderId("123");
        orderCreateResponse.setRedirectUri("http://redirect.uri");
        PaymentData paymentData = new PaymentData(orderCreateResponse.getOrderId(), orderCreateResponse.getRedirectUri());
        ReservationDetails reservationDetails = new ReservationDetails(orderDto.getPaymentId(), paymentData.getUrl());

        when(orderService.placeOrderFromCart()).thenReturn(orderDto);
        when(payU.handle(orderDto)).thenReturn(orderCreateResponse);


        ReservationDetails result = orderController.placeOrderFromCart();

        assertEquals(reservationDetails.getReservationId(), result.getReservationId());
        assertEquals(reservationDetails.getPaymentUrl(), result.getPaymentUrl());
    }

}
