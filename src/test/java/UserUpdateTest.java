import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserUpdateTest extends BaseClassTest {

    @Test
    @DisplayName("Успешное обновление email авторизованного пользователя")
    public void updateUserEmailWithAuthSuccess(){
        UserRequest user = new UserRequest("ALex90@test.ru", "Alex90", "Alexandr");
        createUser(user);

        UserRequest updateUser = new UserRequest("alexandr90@test.ru", null,null);
        Response updateResponse = updateUser(accessToken,updateUser);


        updateResponse.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(updateUser.getEmail().toLowerCase()))
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Успешное обновление имени авторизованного пользователя")
    public void updateUserNameWithAuthSuccess() {
        UserRequest user = new UserRequest("vladimir@test.com", "password123", "Vova");
        createUser(user);

        UserRequest updatedUser = new UserRequest(null, null, "Vladimir");
        Response updateResponse = updateUser(accessToken, updatedUser);

        updateResponse.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.name", equalTo(updatedUser.getName()))
                .body("user.email", equalTo(user.getEmail().toLowerCase()));
    }

    @Test
    @DisplayName("Успешное обновление пароля авторизованного пользователя")
    public void updateUserPasswordWithAuthSuccess() {
        UserRequest user = new UserRequest("viktor88@test.com", "Boiko90", "Viktor");
        createUser(user);

        UserRequest updatedUser = new UserRequest(null, "Boiko123", null);
        Response updateResponse = updateUser(accessToken, updatedUser);

        updateResponse.then()
                .statusCode(200)
                .body("success", equalTo(true));

        // Проверяем авторизацию с новым паролем
        Response loginResponse = loginUser(user.getEmail(), updatedUser.getPassword());
        loginResponse.then().statusCode(200);
    }

    @Test
    @DisplayName("Обновление данных без авторизации : email")
    public void updateEmailWithoutAuthFails(){
        UserRequest user = new UserRequest("Viktor12@test.com", "Boiko78", "Viktor");
        createUser(user);

        UserRequest updateUser = new UserRequest("Viktor89@test.com", null, null);

        // Пытаемся обновить email без авторизации
        given()
                .header("Content-type", "application/json")
                .body(updateUser)
                .when()
                .patch("/auth/user")
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Попытка обновления имени без авторизации")
    public void updateNameWithoutAuthFails() {
        UserRequest user = new UserRequest("Timyr12@test.com", "password123", "Timyr");
        createUser(user);


        // Пытаемся обновить имя без авторизации
        UserRequest updatedUser = new UserRequest(null, null, "Anatoliy");

        given()
                .header("Content-type", "application/json")
                .body(updatedUser)
                .when()
                .patch("/auth/user")
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));

    }

}
