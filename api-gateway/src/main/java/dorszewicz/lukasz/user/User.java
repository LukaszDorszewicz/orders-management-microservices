package dorszewicz.lukasz.user;

import dorszewicz.lukasz.user.dto.GetUserDto;
import dorszewicz.lukasz.user.types.Role;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class User {
    private String id;
    private String username;
    private String password;
    private Role role;
    private boolean isActive;

    public GetUserDto toGetUserDto() {
        return GetUserDto
                .builder()
                .id(id)
                .username(username)
                .password(password)
                .role(role)
                .build();
    }
}
