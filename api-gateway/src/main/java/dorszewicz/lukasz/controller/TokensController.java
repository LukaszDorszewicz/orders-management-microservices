package dorszewicz.lukasz.controller;

import dorszewicz.lukasz.security.dto.RefreshTokenDto;
import dorszewicz.lukasz.security.dto.TokensDto;
import dorszewicz.lukasz.security.tokens.AppTokensService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokensController {
    private final AppTokensService appTokensService;

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.CREATED)
    public TokensDto refreshTokens(@RequestBody RefreshTokenDto refreshTokenDto) {
        return appTokensService.generateTokensFromRefreshToken(refreshTokenDto);
    }
}
