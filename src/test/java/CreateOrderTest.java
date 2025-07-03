

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class CreateOrderTest extends BaseClassTest {


    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void testCreateOrderWithAuthSuccess() {
        createUser(new UserRequest("viktorboiko@test.ru", "qqerty89", "Viktor"));

       createOrder(getBurgerIngredients(),accessToken)
                .then()
                .log()
                .body()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }
    @Test
    @DisplayName("Создание заказа без авторизации")
    public void testCreateOrderWithoutAuth() {
        createOrder(getBurgerIngredients(),"")
                .then()
                .log()
                .body()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов - ошибка 400")
    public void testCreateOrderWithoutIngredients() {
        createUser(new UserRequest("frodo1236@test.ru", "LordOfRings", "Sauron"));

       createOrder(new String[]{},accessToken)
                .then()
                .log()
                .body()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с невалидными ингредиентами - ошибка 500")
    public void testCreateOrderWithInvalidIngredients() {
        createUser(new UserRequest("Griha@test.ru", "PAassWWorf90", "Harru"));

        String[] invalidIngredients = {"inv0c5a71d1f81", "inv1c0c5a71d1f820012"};

      createOrder(invalidIngredients,accessToken)
                .then()
                .log()
                .body()
                .statusCode(500);
    }
}