import io.restassured.response.ValidatableResponse;
import org.example.Courier;
import org.example.CourierClient;
import org.example.CourierCredentials;
import org.example.CourierGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CreateCourierTest {

    private Courier courier;

    private CourierClient CourierClient;

    private static final String MESSAGE_NOT_ENOUGH_DATA = "Недостаточно данных для создания учетной записи";

    private int id;

    @Before
    public void setUp() {
        courier = CourierGenerator.getCourier();
        CourierClient = new CourierClient();
    }

    @After
    public void cleanUp() {
        if (id != 0) {
            ValidatableResponse deleteResponse = CourierClient.delete(id);
            int statusCode = deleteResponse.extract().statusCode();
            assertEquals(SC_OK, statusCode);
        }
    }



    @Test
    //Можно создать курьера
    public void CreateNewCourier(){
        ValidatableResponse response = CourierClient.create(courier);
        ValidatableResponse loginResponse = CourierClient.login(CourierCredentials.from(courier));

        id = loginResponse.extract().path("id");
        int statusCode = response.extract().statusCode();

        assertEquals(SC_CREATED, statusCode);
    }

    @Test
    //Невозможно создать юзера с существующим логином
    public void MustNotCreateNewCourierWithExistingLogin(){
        Courier existingCourier = CourierGenerator.getExistingCourier();
        ValidatableResponse response = CourierClient.create(existingCourier);

        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");

        assertEquals(SC_CONFLICT, statusCode);
        assertEquals(message, "Этот логин уже используется");
    }

    @Test
    //Невозможно создать юзера без логина
    public void CreateNewCourierWithoutLogin(){
        Courier courierWithoutLogin = CourierGenerator.getCourierWithoutLogin();
        ValidatableResponse response = CourierClient.create(courierWithoutLogin);

        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");

        assertEquals(SC_BAD_REQUEST, statusCode);
        assertEquals(message, MESSAGE_NOT_ENOUGH_DATA);

    }

    @Test
    //Невозможно создать юзера без пароля
    public void CreateNewCourierWithoutPassword(){
        Courier courierWithoutPassword = CourierGenerator.getCourierWithoutPassword();
        ValidatableResponse response = CourierClient.create(courierWithoutPassword);

        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");

        assertEquals(SC_BAD_REQUEST, statusCode);
        assertEquals(message, MESSAGE_NOT_ENOUGH_DATA);

    }

    @Test
    //Можно создать юзера без firstName
    public void CreateNewCourierWithoutFirstName(){
        Courier courierWithoutFirstName = CourierGenerator.getCourierWithoutFirstName();
        ValidatableResponse response = CourierClient.create(courierWithoutFirstName);
        ValidatableResponse loginResponse = CourierClient.login(CourierCredentials.from(courier));

        int statusCode = response.extract().statusCode();
        id = loginResponse.extract().path("id");

        assertEquals(SC_CREATED, statusCode);

    }

    @Test
    // Успешный запрос возвращает ок тру
    public void SuccesfullRequestReturnsValueOkTrue(){
        Courier courierWithoutFirstName = CourierGenerator.getCourierWithoutFirstName();
        ValidatableResponse response = CourierClient.create(courierWithoutFirstName);
        ValidatableResponse loginResponse = CourierClient.login(CourierCredentials.from(courier));

        boolean ok_field = response.extract().path("ok");
        id = loginResponse.extract().path("id");

        assertTrue(ok_field);

    }
}


