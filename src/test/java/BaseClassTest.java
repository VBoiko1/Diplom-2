import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


import static io.restassured.RestAssured.given;

public class BaseClassTest {
    protected static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";
    protected String accessToken;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @AfterEach
    public void tearDown() {
        if (accessToken != null) {
            deleteUser(accessToken);
        }
    }

    @Step("Создание пользователя")
    protected Response createUser(UserRequest user) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post("/auth/register");
        this.accessToken = response.path("accessToken");
        return response;
    }


    @Step("Удаление пользователя")
    protected void deleteUser(String accessToken) {
        given()
                .header("Authorization", accessToken)
                .when()
                .delete("/auth/user")
                .then()
                .statusCode(202);
    }

    @Step("Авторизация пользователя")
    protected Response loginUser(String email, String password){
        return given()
                .header("Content-type", "application/json")
                .body(new AuthRequest(email,password))
                .when()
                .post("/auth/login");
    }

    @Step("Обновление данных пользователя")
    protected Response updateUser(String accessToken, UserRequest updateUser){
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(updateUser)
                .when()
                .patch("/auth/user");
    }

    @Step("Получение валидных ингредиентов")
    protected String[] getBurgerIngredients() {
        Response response = given()
                .get("/ingredients");

        String bun = response.jsonPath().getString("data.find { it.type == 'bun' }._id");
        String main = response.jsonPath().getString("data.find { it.type == 'main' }._id");
        String sauce = response.jsonPath().getString("data.find { it.type == 'sauce' }._id");

        return new String[]{bun, main, sauce, bun};
    }

    @Step("Создание заказа")
    public Response createOrder(String[] ingredients, String accessToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(new OrderRequest(ingredients))
                .when()
                .post("/orders");
    }
}
