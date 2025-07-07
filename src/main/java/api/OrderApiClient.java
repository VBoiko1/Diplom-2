package api;

import io.restassured.response.Response;
import model.OrderRequest;
import io.restassured.path.json.JsonPath;
public class OrderApiClient extends RestClient{
    public Response createOrder(String[] ingredients, String accessToken) {
        return getBaseRequest()
                .header("Authorization", accessToken)
                .body(new OrderRequest(ingredients))
                .post("/orders");
    }

    public Response getUserOrders(String accessToken) {
        return getBaseRequest()
                .header("Authorization", accessToken)
                .get("/orders");
    }

    public String[] getIngredients() {
        Response response = getBaseRequest()
                .get("/ingredients");

        String bun = response.jsonPath().getString("data.find { it.type == 'bun' }._id");
        String main = response.jsonPath().getString("data.find { it.type == 'main' }._id");
        String sauce = response.jsonPath().getString("data.find { it.type == 'sauce' }._id");

        return new String[]{bun, main, sauce, bun};
    }
}
