package model;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class UserRequest {
    private String email;
    private String password;
    private String name;
}