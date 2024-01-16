package br.com.gspadilha.gestaodevagas.module.useCases;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.gspadilha.gestaodevagas.module.dto.AuthCompanyDTO;
import br.com.gspadilha.gestaodevagas.module.repositories.CompanyRepository;

@Service
public class AuthCompanyUseCase {

    @Value("${jwt.security.secret.token}")
    private String secretKey;

    @Value("${jwt.security.issuer}")
    private String issuer;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException {
        var company = this.companyRepository.findByUsername(authCompanyDTO.getUsername()).orElseThrow(
                () -> {
                    throw new UsernameNotFoundException("User from company not found");
                });

        var passwordsMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

        if (!passwordsMatches) {
            throw new AuthenticationException();
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create().withIssuer(issuer)
                .withSubject(company.getId().toString())
                .withExpiresAt(Instant.now().plus(Duration.ofHours(2)))
                .sign(algorithm);
    }
}
