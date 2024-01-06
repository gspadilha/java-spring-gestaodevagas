package br.com.gspadilha.gestaodevagas.module.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gspadilha.gestaodevagas.exceptions.UserFoundException;
import br.com.gspadilha.gestaodevagas.module.entities.CandidateEntity;
import br.com.gspadilha.gestaodevagas.module.repositories.CandidateRepository;

@Service
public class CreateCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    public CandidateEntity execute(CandidateEntity candidateEntity) {
        var username = candidateEntity.getUsername();
        var email = candidateEntity.getEmail();

        this.candidateRepository
                .findByUsernameOrEmail(username, email)
                .ifPresent(user -> {
                    throw new UserFoundException();
                });

        return this.candidateRepository.save(candidateEntity);
    }
}
