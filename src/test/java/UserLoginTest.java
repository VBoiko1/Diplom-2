import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserLoginTest extends BaseClassTest {

    @Test
    @DisplayName("Успешная авторизация")
    public void successfulLoginTest(){
        UserRequest user = new UserRequest("Frodo@test.ru","RingIsMy1","Frodo");
        createUser(user);

        Response loginResponse = loginUser(user.getEmail(), user.getPassword());

        loginResponse.then()
                .statusCode(200)
                .body("success",equalTo(true))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken",not(emptyString()))
                .body("user.email",equalTo(user.getEmail().toLowerCase()))
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Авторизация с неверным email")
    public void loginWithInvalidEmail(){
        UserRequest user = new UserRequest("Stenli@test.ru","Qwerty89","Sten");
        createUser(user);

        given()
                .header("Content-type","application/json")
                .body(new AuthRequest("Stepan@test.ru",user.getPassword()))
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401)
                .body("success",equalTo(false))
                .body("message",equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void loginWithInvalidPassword(){
        UserRequest user = new UserRequest("Tom@test.ru","Moscow89","Tomoson");
        createUser(user);

        given()
                .header("Content-type","application/json")
                .body(new AuthRequest(user.getEmail(), "QWerty67"))
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401)
                .body("success",equalTo(false))
                .body("message",equalTo("email or password are incorrect"));
    }
}
