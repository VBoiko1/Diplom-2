package model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderResponse {
    private boolean success;
    private String name;
    private Order order;

    @Data
    @NoArgsConstructor
    public static class Order {
        private int number;
    }
}