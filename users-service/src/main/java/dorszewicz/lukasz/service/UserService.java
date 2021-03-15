package dorszewicz.lukasz.service;

import dorszewicz.lukasz.exception.AppSecurityException;
import dorszewicz.lukasz.repository.UserRepository;
import dorszewicz.lukasz.user.dto.CreateUserDto;
import dorszewicz.lukasz.user.dto.GetUserDto;
import dorszewicz.lukasz.validation.CreateUserDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CreateUserDtoValidator createUserDtoValidator;
    private final PasswordEncoder passwordEncoder;

    public GetUserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppSecurityException("User with username: " + username + " not found"))
                .toGetUserDto();
    }

    public GetUserDto findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppSecurityException("User with id: " + id + " not found"))
                .toGetUserDto();
    }

    public String registerUser(CreateUserDto createUserDto) {

        var errors = createUserDtoValidator.validate(createUserDto);
        if (createUserDtoValidator.hasErrors()) {
            var errorsMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + ": " + e.getValue())
                    .collect(Collectors.joining(", "));
            throw new ValidationException(errorsMessage);
        }

        var userFromDb = userRepository.findByUsername(createUserDto.getUsername());

        if (userFromDb.isPresent()) {
            throw new ValidationException("Username is already in use");
        }

        var userToRegister = CreateUserDto
                .builder()
                .username(createUserDto.getUsername())
                .password(createUserDto.getPassword())
                .passwordConfirmation(createUserDto.getPasswordConfirmation())
                .role(createUserDto.getRole())
                .build();

        userToRegister.setPassword(passwordEncoder.encode(userToRegister.getPassword()));

        var insertedUser = userRepository.save(userToRegister.toUser());
        return insertedUser.toGetUserDto().getId();
    }
}
