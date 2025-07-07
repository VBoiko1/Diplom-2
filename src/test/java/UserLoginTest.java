import io.restassured.response.Response;
import model.AuthRequest;
import model.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

public class UserLoginTest extends BaseClassTest {

    @Test
    @DisplayName("Успешная авторизация")
    public void successfulLoginTest() {
        UserRequest user = createRandomUser();
        registerUser(user);

        Response loginResponse = userApi.loginUser(new AuthRequest(user.getEmail(), user.getPassword()));
        verifySuccessResponse(loginResponse);
        this.accessToken = loginResponse.path("accessToken");
    }

    @Test
    @DisplayName("Авторизация с неверным email")
    public void loginWithInvalidEmail() {
        UserRequest user = createRandomUser();
        registerUser(user);

        Response response = userApi.loginUser(new AuthRequest(
                faker.internet().emailAddress(),
                user.getPassword()
        ));

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void loginWithInvalidPassword() {
        UserRequest user = createRandomUser();
        registerUser(user);

        Response response = userApi.loginUser(new AuthRequest(
                user.getEmail(),
                faker.internet().password()
        ));

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}