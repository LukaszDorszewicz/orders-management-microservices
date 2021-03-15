package dorszewicz.lukasz.controller;

import dorszewicz.lukasz.proxy.UserServiceProxy;
import dorszewicz.lukasz.user.dto.CreateUserDto;
import dorszewicz.lukasz.user.dto.GetUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserServiceProxy userServiceProxy;

    @GetMapping("/find/username/{username}")
    GetUserDto findByName(@PathVariable("username") String name) {
        return userServiceProxy.findByUsername(name);
    }

    @GetMapping("/find/id/{id}")
    GetUserDto findById(@PathVariable("id") String id) {
        return userServiceProxy.findById(id);
    }

    @PostMapping("/register")
    String register(@RequestBody CreateUserDto createUserDto) {
        return userServiceProxy.registerNewUser(createUserDto);
    }
}
