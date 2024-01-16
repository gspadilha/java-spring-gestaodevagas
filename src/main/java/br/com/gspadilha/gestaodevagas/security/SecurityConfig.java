package br.com.gspadilha.gestaodevagas.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    List<String> routesPermitted = new ArrayList<>(Arrays.asList("/candidate/", "/company/"));

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    for (String route : routesPermitted) {
                        auth.requestMatchers(route).permitAll();
                    }
                    auth.anyRequest().authenticated();
                });
        return http.build();
    }
}
