public class OrderRequest {
    private String[] ingredients;


    public OrderRequest() {
    }
    // Конструктор с параметрами
    public OrderRequest(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }
}
