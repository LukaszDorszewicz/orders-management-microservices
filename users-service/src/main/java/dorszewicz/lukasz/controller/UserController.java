package dorszewicz.lukasz.controller;

import dorszewicz.lukasz.service.UserService;
import dorszewicz.lukasz.user.dto.CreateUserDto;
import dorszewicz.lukasz.user.dto.GetUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user/find/username/{username}")
    public GetUserDto findByUsername(@PathVariable("username") String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/user/find/id/{id}")
    public GetUserDto findById(@PathVariable("id") String id) {
        return userService.findById(id);
    }

    @PostMapping("/user/register")
    public String registerNewUser(@RequestBody CreateUserDto createUserDto) {
        return userService.registerUser(createUserDto);
    }
}
