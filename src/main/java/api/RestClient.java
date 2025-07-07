package api;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestClient {

    protected static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";

    public RestClient() {
        RestAssured.baseURI = BASE_URL;
    }

    protected RequestSpecification getBaseRequest() {
        return given()
                .header("Content-type", "application/json");
    }
}
