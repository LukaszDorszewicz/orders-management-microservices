package dorszewicz.lukasz.user.dto;

import dorszewicz.lukasz.user.User;
import dorszewicz.lukasz.user.types.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserDto {
    private String username;
    private String password;
    private String passwordConfirmation;
    private Role role;

    public User toUser() {
        return User
                .builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
    }
}
