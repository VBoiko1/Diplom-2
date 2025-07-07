import model.AuthRequest;
import model.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

public class CreateOrderTest extends BaseClassTest {

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void testCreateOrderWithAuthSuccess() {
        UserRequest user = createRandomUser();
        registerAndLoginUser(user);
        createOrder(getIngredients(), accessToken);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void testCreateOrderWithoutAuth() {
        createOrder(getIngredients(), "");
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов - ошибка 400")
    public void testCreateOrderWithoutIngredients() {
        UserRequest user = createRandomUser();
        registerAndLoginUser(user);

        orderApi.createOrder(new String[]{}, accessToken)
                .then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с невалидными ингредиентами - ошибка 500")
    public void testCreateOrderWithInvalidIngredients() {
        UserRequest user = createRandomUser();
        registerAndLoginUser(user);

        String[] invalidIngredients = {"invalid1", "invalid2"};
        orderApi.createOrder(invalidIngredients, accessToken)
                .then()
                .statusCode(500);
    }
}
