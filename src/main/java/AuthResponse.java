public class AuthResponse {
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private User user;

    public static class User {
        private String email;
        private String name;

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public User getUser() {
        return user;
    }
}