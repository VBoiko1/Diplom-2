package model;

import lombok.Data;

@Data
public class UserResponse {
    private boolean success;
    private String message;
    private UserData user;
    private String accessToken;
    private String refreshToken;

    @Data
    public class UserData {
        private String email;
        private String name;
    }
}