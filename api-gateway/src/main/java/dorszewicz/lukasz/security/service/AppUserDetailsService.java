package dorszewicz.lukasz.security.service;

import dorszewicz.lukasz.proxy.UserServiceProxy;
import dorszewicz.lukasz.security.exception.AppSecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserServiceProxy userServiceProxy;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userServiceProxy.findByUsername(username);
        if (user == null) {
            throw new AppSecurityException("Username not found");
        }
        return new User(
                user.getUsername(),
                user.getPassword(),
                true, true, true, true,
                Collections.emptyList());
    }
}
