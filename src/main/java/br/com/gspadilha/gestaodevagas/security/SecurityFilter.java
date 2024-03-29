package br.com.gspadilha.gestaodevagas.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.gspadilha.gestaodevagas.providers.JWTProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // SecurityContextHolder.getContext().setAuthentication(null);

        this.checkValidity(request, response);

        filterChain.doFilter(request, response);
    }

    private void checkValidity(HttpServletRequest request, HttpServletResponse response) {
        if (!request.getRequestURI().startsWith("/company")) {
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            return;
        }

        var token = this.jwtProvider.validateToken(authHeader);

        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        request.setAttribute("company_id", token.getSubject());

        var roles = token.getClaim("roles").asList(Object.class);

        var grants = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()))
                .toList();

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                token.getSubject(),
                grants);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
