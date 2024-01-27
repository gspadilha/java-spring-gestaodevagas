package br.com.gspadilha.gestaodevagas.module.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.gspadilha.gestaodevagas.exceptions.UserFoundException;
import br.com.gspadilha.gestaodevagas.module.entities.CandidateEntity;
import br.com.gspadilha.gestaodevagas.module.repositories.CandidateRepository;

@Service
public class CreateCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CandidateEntity execute(CandidateEntity candidateEntity) {
        var username = candidateEntity.getUsername();
        var email = candidateEntity.getEmail();

        this.candidateRepository
                .findByUsernameOrEmail(username, email)
                .ifPresent(user -> {
                    throw new UserFoundException();
                });

        var password = passwordEncoder.encode(candidateEntity.getPassword());
        candidateEntity.setPassword(password);

        return this.candidateRepository.save(candidateEntity);
    }
}
