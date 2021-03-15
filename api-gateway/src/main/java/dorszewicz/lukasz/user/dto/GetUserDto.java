package dorszewicz.lukasz.user.dto;

import dorszewicz.lukasz.user.types.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserDto {
    private String id;
    private String username;
    private String password;
    private Role role;
}
