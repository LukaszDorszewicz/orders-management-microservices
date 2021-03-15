package dorszewicz.lukasz.user;

import dorszewicz.lukasz.user.dto.GetUserDto;
import dorszewicz.lukasz.user.types.Role;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Document(collection = "users")
public class User {
    private String id;
    private String username;
    private String password;
    private Role role;

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
