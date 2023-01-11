import io.restassured.response.ValidatableResponse;
import org.example.OrderClient;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListOrdersTest {


    private OrderClient OrderClient;

    @Before
    public void setUp() {
        OrderClient = new OrderClient();
    }

    @Test
    // Код 200 на запрос списка заказов
    public void successfulGetOrderReturnStatusCode200(){
        ValidatableResponse response = OrderClient.getOrders();
        int statusCode = response.extract().statusCode();

        assertEquals(SC_OK, statusCode);
    }

    @Test
    // Запрос на список заказов возвращает не пустой массив orders
    public void successfulGetOrderReturnsNotEmptyArray(){
        ValidatableResponse response = OrderClient.getOrders();

        ArrayList orders = response.extract().path("orders");

        assertTrue(orders.size()  > 0 );
    }
}
