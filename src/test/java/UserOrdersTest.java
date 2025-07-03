
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserOrdersTest extends BaseClassTest {

    @Test
    @DisplayName("Получение заказов авторизованного пользователя - успешный сценарий")
    public void testGetUserOrdersWithAuth() {

        createUser(new UserRequest("Federico88@test.ru", "Viktor222", "Fedor"));

        createOrder(getBurgerIngredients(), accessToken);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get("/orders")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", not(empty()))
                .body("orders[0].number", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя - ошибка 401")
    public void testGetUserOrdersWithoutAuth() {
        given()
                .header("Content-type", "application/json")
                .when()
                .get("/orders")
                .then()
                .log().all()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}