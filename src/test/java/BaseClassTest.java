import api.OrderApiClient;
import api.UserApiClient;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.AuthRequest;
import model.UserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.Matchers.*;

public class BaseClassTest {
    protected UserApiClient userApi;
    protected OrderApiClient orderApi;
    protected String accessToken;
    protected static final Faker faker = new Faker();

    @BeforeEach
    public void setUp() {
        userApi = new UserApiClient();
        orderApi = new OrderApiClient();
    }

    @AfterEach
    public void tearDown() {
        if (accessToken != null) {
            deleteUser();
        }
    }

    // User steps
    @Step("Создание случайного пользователя")
    protected UserRequest createRandomUser() {
        return new UserRequest(
                faker.internet().emailAddress(),
                faker.internet().password(8, 16),
                faker.name().fullName()
        );
    }

    @Step("Регистрация пользователя")
    protected void registerUser(UserRequest user) {
        userApi.createUser(user);
    }

    @Step("Авторизация пользователя")
    protected void loginUser(UserRequest user) {
        this.accessToken = userApi.loginUser(new AuthRequest(user.getEmail(), user.getPassword()))
                .path("accessToken");
    }

    @Step("Регистрация и авторизация пользователя")
    protected void registerAndLoginUser(UserRequest user) {
        registerUser(user);
        loginUser(user);
    }

    @Step("Удаление пользователя")
    protected void deleteUser() {
        userApi.deleteUser(accessToken);
    }

    @Step("Обновление данных пользователя")
    protected Response updateUser(UserRequest updateData) {
        return userApi.updateUser(accessToken, updateData);
    }

    // Order steps
    @Step("Создание заказа")
    protected void createOrder(String[] ingredients, String token) {
        orderApi.createOrder(ingredients, token)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Step("Получение списка заказов")
    protected Response getUserOrders(String token) {
        return orderApi.getUserOrders(token);
    }

    @Step("Получение ингредиентов")
    protected String[] getIngredients() {
        return orderApi.getIngredients();
    }

    // Verification steps
    @Step("Проверка успешного ответа")
    protected void verifySuccessResponse(Response response) {
        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Step("Проверка ошибки авторизации")
    protected void verifyAuthError(Response response) {
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}