import io.restassured.response.Response;
import model.AuthRequest;
import model.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

public class UserRegistrationTest extends BaseClassTest {

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserSuccessfully() {
        UserRequest user = createRandomUser();
        Response response = userApi.createUser(user);
        verifySuccessResponse(response);
        this.accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    public void createExistingUserFails() {
        UserRequest user = createRandomUser();
        registerUser(user);

        Response duplicateResponse = userApi.createUser(user);
        duplicateResponse.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmailFails() {
        Response response = userApi.createUser(
                new UserRequest(null, faker.internet().password(), faker.name().fullName())
        );

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPasswordFails() {
        Response response = userApi.createUser(
                new UserRequest(faker.internet().emailAddress(), null, faker.name().fullName())
        );

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameFails() {
        Response response = userApi.createUser(
                new UserRequest(faker.internet().emailAddress(), faker.internet().password(), null)
        );


        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}