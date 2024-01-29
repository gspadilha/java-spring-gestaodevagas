package br.com.gspadilha.gestaodevagas.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.gspadilha.gestaodevagas.providers.JWTCandidateProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityCandidateFilter extends OncePerRequestFilter {

    @Autowired
    JWTCandidateProvider jwtCandidateProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // SecurityContextHolder.getContext().setAuthentication(null);

        this.checkValidity(request, response);

        filterChain.doFilter(request, response);
    }

    private void checkValidity(HttpServletRequest request, HttpServletResponse response) {
        if (!request.getRequestURI().startsWith("/candidate")) {
            return;
        }

        String header = request.getHeader("Authorization");

        if (header == null) {
            return;
        }

        var token = this.jwtCandidateProvider.validateToken(header);

        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        request.setAttribute("candidate_id", token.getSubject());

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
