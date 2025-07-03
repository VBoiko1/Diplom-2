
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalTo;

public class UserRegistrationTest extends BaseClassTest {

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserSuccessfully() {
        UserRequest user = new UserRequest("Boiko89@test.ru", "Boiko891", "Viktor");
        Response response = createUser(user);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail().toLowerCase()))
                .body("user.name", equalTo(user.getName()));


    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    public void createExistingUserFails() {
        UserRequest user = new UserRequest("Boiko90@test.ru", "Boiko891", "Ivan");
        //Создание уникального пользователя
        createUser(user);
        //Создание пользователя с такими же данными
        Response duplicateResponse  = createUser(user);

        duplicateResponse .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));

    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmailFails() {
        UserRequest user = new UserRequest(null, "Boiko891", "Samanta");
        Response response = createUser(user);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPasswordFails() {
        UserRequest user = new UserRequest("Harry@test.ru",null,"Harry");
        Response response = createUser(user);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameFails(){
        UserRequest user = new UserRequest("Dava@test.ru","David123",null);
        Response response = createUser(user);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message",equalTo("Email, password and name are required fields"));
    }


}
