package br.com.gspadilha.gestaodevagas.module.useCases;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.gspadilha.gestaodevagas.module.dto.AuthCandidateRequestDTO;
import br.com.gspadilha.gestaodevagas.module.dto.AuthCandidateResponseDTO;
import br.com.gspadilha.gestaodevagas.module.repositories.CandidateRepository;

@Service
public class AuthCandidateUseCase {

    @Value("${jwt.security.secret.token.candidate}")
    private String secretKey;

    @Value("${jwt.security.issuer}")
    private String issuer;

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTO)
            throws AuthenticationException {
        var candidate = this.candidateRepository.findByUsername(authCandidateRequestDTO.username())
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("Usuário/Senha incorretos!");
                });

        var passwordMatches = this.passwordEncoder.matches(authCandidateRequestDTO.password(), candidate.getPassword());

        if (!passwordMatches) {
            throw new AuthenticationException();
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        var expiresIn = Instant.now().plus(Duration.ofMinutes(60));

        var token = JWT.create()
                .withIssuer(issuer)
                .withSubject(candidate.getId().toString())
                .withExpiresAt(expiresIn)
                .withClaim("role", Arrays.asList("candidate"))
                .sign(algorithm);

        return AuthCandidateResponseDTO.builder()
                .access_token(token)
                .expires_in(expiresIn.toEpochMilli())
                .build();
    }
}
