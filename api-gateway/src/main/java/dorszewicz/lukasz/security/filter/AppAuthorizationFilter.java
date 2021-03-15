package dorszewicz.lukasz.security.filter;

import dorszewicz.lukasz.security.tokens.AppTokensService;
import org.apache.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AppAuthorizationFilter extends BasicAuthenticationFilter {

    private final AppTokensService appTokensService;

    public AppAuthorizationFilter(AuthenticationManager authenticationManager, AppTokensService appTokensService) {
        super(authenticationManager);
        this.appTokensService = appTokensService;
    }

    // ponizsza metode wyluska z requesta header i przekaze go do parsowania
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        var header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null) {
            var authorizedUser = appTokensService.parseAccessToken(header);
            SecurityContextHolder.getContext().setAuthentication(authorizedUser);
        }

        // to wywolaie mowi ze filter ma przekierowac obiekty request oraz response
        // do kolejnego filtra w lancuchu wywolan filtra
        chain.doFilter(request, response);
    }
}
