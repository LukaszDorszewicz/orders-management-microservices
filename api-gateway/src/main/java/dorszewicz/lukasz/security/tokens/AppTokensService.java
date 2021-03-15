package dorszewicz.lukasz.security.tokens;

import dorszewicz.lukasz.proxy.UserServiceProxy;
import dorszewicz.lukasz.security.dto.TokensDto;
import dorszewicz.lukasz.security.exception.AppSecurityException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppTokensService {

    @Value("${tokens.access-token.expiration-time-ms}")
    private Long accessTokenExpirationTimeMs;

    @Value("${tokens.refresh-token.expiration-time-ms}")
    private Long refreshTokenExpirationTimeMs;

    @Value("${tokens.refresh-token.property}")
    private String refreshTokenProperty;

    @Value("${tokens.prefix}")
    private String tokensPrefix;

    private final SecretKey secretKey;
    private final UserServiceProxy userServiceProxy;

    // --------------------------------------------------------------------------------------------------------
    // GENEROWANIE TOKENA
    // --------------------------------------------------------------------------------------------------------
    public TokensDto generateTokens(Authentication authentication) {
        if (authentication == null) {
            throw new AppSecurityException("authentication object is null");
        }

        var user = userServiceProxy.findByUsername(authentication.getName());
        if (user == null) {
            throw new AppSecurityException("User not found");
        }

        var userId = user.getId();
        var currentDate = new Date();
        var accessTokenExpirationDate = new Date(currentDate.getTime() + accessTokenExpirationTimeMs);
        var refreshTokenExpirationDate = new Date(currentDate.getTime() + refreshTokenExpirationTimeMs);

        var accessToken = Jwts
                .builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(accessTokenExpirationDate)
                .setIssuedAt(currentDate)
                .signWith(secretKey)
                .compact();

        var refreshToken = Jwts
                .builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(refreshTokenExpirationDate)
                .setIssuedAt(currentDate)
                .signWith(secretKey)
                .claim(refreshTokenProperty, accessTokenExpirationDate.getTime())
                .compact();

        return TokensDto
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // --------------------------------------------------------------------------------------------------------
    // PARSOWANIE TOKENA
    // --------------------------------------------------------------------------------------------------------

    public Authentication parseAccessToken(String header) {
        if (header == null) {
            throw new AppSecurityException("token is null");
        }

        if (!header.startsWith(tokensPrefix)) {
            throw new AppSecurityException("token has incorrect format");
        }

        var accessToken = header.replace(tokensPrefix, "");

        if (!isTokenValid(accessToken)) {
            throw new AppSecurityException("tokena has been expired");
        }

        var userId = id(accessToken);
        var user = userServiceProxy.findById(userId);
        if (user == null) {
            throw new AppSecurityException("User not found");
        }

        return new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                List.of(new SimpleGrantedAuthority(user.getRole().toString()))
        );
    }

    private Claims claims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String id(String token) {
        return claims(token).getSubject();
    }

    private Date expirationDate(String token) {
        return claims(token).getExpiration();
    }

    private boolean isTokenValid(String token) {
        return expirationDate(token).after(new Date());
    }

    private boolean isRefreshIsPermitted(String token) {
        return Long.parseLong(claims(token).get(refreshTokenProperty).toString()) > System.currentTimeMillis();
    }
}
