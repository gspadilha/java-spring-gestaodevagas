package br.com.gspadilha.gestaodevagas.module.useCases;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.gspadilha.gestaodevagas.module.dto.AuthCandidateResponseDTO;
import br.com.gspadilha.gestaodevagas.module.dto.AuthCompanyDTO;
import br.com.gspadilha.gestaodevagas.module.repositories.CompanyRepository;

@Service
public class AuthCompanyUseCase {

    @Value("${jwt.security.secret.token.candidate}")
    private String secretKey;

    @Value("${jwt.security.issuer}")
    private String issuer;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthCandidateResponseDTO execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException {
        var company = this.companyRepository.findByUsername(authCompanyDTO.getUsername()).orElseThrow(
                () -> {
                    throw new UsernameNotFoundException("User from company not found");
                });

        var passwordsMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

        if (!passwordsMatches) {
            throw new AuthenticationException();
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        var expiresIn = Instant.now().plus(Duration.ofHours(2));

        var token = JWT.create()
                .withIssuer(issuer)
                .withSubject(company.getId().toString())
                .withExpiresAt(expiresIn)
                .withClaim("roles", Arrays.asList("COMPANY"))
                .sign(algorithm);

        return AuthCandidateResponseDTO.builder()
                .access_token(token)
                .expires_in(expiresIn.toEpochMilli())
                .build();
    }
}
