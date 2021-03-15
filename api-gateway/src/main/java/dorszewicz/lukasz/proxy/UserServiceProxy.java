package dorszewicz.lukasz.proxy;

import dorszewicz.lukasz.user.dto.CreateUserDto;
import dorszewicz.lukasz.user.dto.GetUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "users-service")
public interface UserServiceProxy {

    @GetMapping("/user/find/username/{username}")
    GetUserDto findByUsername(@PathVariable("username") String username);

    @GetMapping("/user/find/id/{id}")
    GetUserDto findById(@PathVariable("id") String id);

    @PostMapping("/user/register")
    String registerNewUser(@RequestBody CreateUserDto createUserDto);

    @GetMapping("/user/findAll")
    List<GetUserDto> findAll();
}
