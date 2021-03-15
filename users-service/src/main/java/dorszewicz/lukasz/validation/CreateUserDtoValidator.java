package dorszewicz.lukasz.validation;

import dorszewicz.lukasz.user.dto.CreateUserDto;
import dorszewicz.lukasz.user.types.Role;
import dorszewicz.lukasz.validation.generic.AbstractValidator;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Component
public class CreateUserDtoValidator extends AbstractValidator<CreateUserDto> {

    @Override
    public Map<String, String> validate(CreateUserDto createUserDto) {
        errors.clear();

        if (Objects.isNull(createUserDto)) {
            throw new ValidationException("User object is null");
        }

        if (!Objects.equals(createUserDto.getPassword(), createUserDto.getPasswordConfirmation())) {
            errors.put("password", "invalid");
        }

        if (Arrays.stream(Role.values()).noneMatch(role -> role.equals(Role.valueOf(createUserDto.getRole().toString())))) {
            errors.put("role", "invalid");
        }

        return errors;
    }
}
