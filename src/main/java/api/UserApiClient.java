package api;

import io.restassured.response.Response;
import model.AuthRequest;
import model.UserRequest;

public class UserApiClient extends RestClient {
    public Response createUser(UserRequest user) {
        return getBaseRequest()
                .body(user)
                .post("/auth/register");
    }

    public Response loginUser(AuthRequest authRequest) {
        return getBaseRequest()
                .body(authRequest)
                .post("/auth/login");
    }

    public Response updateUser(String accessToken, UserRequest updateData) {
        return getBaseRequest()
                .header("Authorization", accessToken)
                .body(updateData)
                .patch("/auth/user");
    }

    public void deleteUser(String accessToken) {
        getBaseRequest()
                .header("Authorization", accessToken)
                .delete("/auth/user")
                .then()
                .statusCode(202);
    }
}
