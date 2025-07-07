import io.restassured.response.Response;
import model.AuthRequest;
import model.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserUpdateTest extends BaseClassTest {

    @Test
    @DisplayName("Успешное обновление email авторизованного пользователя")
    public void updateUserEmailWithAuthSuccess() {
        UserRequest user = createRandomUser();
        registerAndLoginUser(user);

        UserRequest updateData = new UserRequest(faker.internet().emailAddress(), null, null);
        Response updateResponse = updateUser(updateData);

        verifySuccessResponse(updateResponse);
        updateResponse.then()
                .body("user.email", equalTo(updateData.getEmail().toLowerCase()));
    }

    @Test
    @DisplayName("Успешное обновление имени авторизованного пользователя")
    public void updateUserNameWithAuthSuccess() {
        UserRequest user = createRandomUser();
        registerAndLoginUser(user);


        String newName = faker.name().fullName();
        UserRequest updateData = new UserRequest(null, null, newName);
        Response updateResponse = updateUser(updateData);

        verifySuccessResponse(updateResponse);
        updateResponse.then()
                .body("user.name", equalTo(newName))
                .body("user.email", equalTo(user.getEmail().toLowerCase()));
    }

    @Test
    @DisplayName("Успешное обновление пароля авторизованного пользователя")
    public void updateUserPasswordWithAuthSuccess() {
        UserRequest user = createRandomUser();
        registerAndLoginUser(user);

        String newPassword = faker.internet().password(10, 16);
        UserRequest updateData = new UserRequest(null, newPassword, null);
        Response updateResponse = updateUser(updateData);


        verifySuccessResponse(updateResponse);


        userApi.loginUser(new AuthRequest(user.getEmail(), newPassword))
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Обновление email без авторизации")
    public void updateEmailWithoutAuthFails() {
        UserRequest user = createRandomUser();
        registerAndLoginUser(user);

        UserRequest updateData = new UserRequest(faker.internet().emailAddress(), null, null);

        given()
                .header("Content-type", "application/json")
                .body(updateData)
                .when()
                .patch("/auth/user")
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Обновление имени без авторизации")
    public void updateNameWithoutAuthFails() {

        UserRequest user = createRandomUser();
        registerAndLoginUser(user);

        UserRequest updateData = new UserRequest(null, null, faker.name().fullName());

        given()
                .header("Content-type", "application/json")
                .body(updateData)
                .when()
                .patch("/auth/user")
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
    @Test
    @DisplayName("Обновление пароля без авторизации")
    void updatePasswordWithoutAuthFails() {
        UserRequest user = createRandomUser();
        registerAndLoginUser(user);

        UserRequest updateData = new UserRequest(null, faker.internet().password(), null);

        given()
                .header("Content-type", "application/json")
                .body(updateData)
                .when()
                .patch("/auth/user")
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
