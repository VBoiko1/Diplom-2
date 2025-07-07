import io.restassured.response.Response;
import model.AuthRequest;
import model.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

public class UserOrdersTest extends BaseClassTest {

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void testGetUserOrdersWithAuth() {
        UserRequest user = createRandomUser();
        registerAndLoginUser(user);
        createOrder(getIngredients(), accessToken);

        Response ordersResponse = getUserOrders(accessToken);
        verifySuccessResponse(ordersResponse);
        ordersResponse.then()
                .body("orders", not(empty()))
                .body("orders[0].number", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя - ошибка 401")
    public void testGetUserOrdersWithoutAuth() {
        getUserOrders("")
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}