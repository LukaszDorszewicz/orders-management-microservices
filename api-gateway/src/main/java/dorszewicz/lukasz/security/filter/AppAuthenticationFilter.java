package dorszewicz.lukasz.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dorszewicz.lukasz.security.dto.AuthenticationDto;
import dorszewicz.lukasz.security.exception.AppSecurityException;
import dorszewicz.lukasz.security.tokens.AppTokensService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

// rozszerzenie o ta klase powoduje ze ten filter bedzie wykonywal sie
// automatycznie na metode POST /login
public class AppAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AppTokensService appTokensService;

    public AppAuthenticationFilter(AuthenticationManager authenticationManager, AppTokensService appTokensService) {
        this.authenticationManager = authenticationManager;
        this.appTokensService = appTokensService;

        // jezeli chcesz zeby za logowanie odpowiadal inny request to mozesz to zmienic
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/app-login", "POST"));
    }

    // ta metoda podejmuje probe logowania czyli jezeli uda sie zalogowac to automatycznie przekieruje Cie do
    // ponizszej metody successfulAuthentication i tam beda wygenerowane tokeny a jak nie uda sie to wyjatek
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            var user = new ObjectMapper().readValue(request.getInputStream(), AuthenticationDto.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.emptyList()
            ));
        } catch (Exception e) {
            throw new AppSecurityException(e.getMessage());
        }
    }

    // jezeli powyzsze logowanie sie powiedzie to zwroc uwage ze jako 4 argument masz Authentication
    // ktory reprezentuje zalogowanego usera
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        var tokens = appTokensService.generateTokens(authResult);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(tokens));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
