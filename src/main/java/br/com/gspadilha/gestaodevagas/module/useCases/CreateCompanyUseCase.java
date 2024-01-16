package br.com.gspadilha.gestaodevagas.module.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.gspadilha.gestaodevagas.exceptions.CompanyFoundException;
import br.com.gspadilha.gestaodevagas.module.entities.CompanyEntity;
import br.com.gspadilha.gestaodevagas.module.repositories.CompanyRepository;

@Service
public class CreateCompanyUseCase {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CompanyEntity execute(CompanyEntity companyEntity) {
        var username = companyEntity.getUsername();
        var email = companyEntity.getEmail();

        this.companyRepository
                .findByUsernameOrEmail(username, email)
                .ifPresent(company -> {
                    throw new CompanyFoundException();
                });

        var password = passwordEncoder.encode(companyEntity.getPassword());
        companyEntity.setPassword(password);

        return this.companyRepository.save(companyEntity);
    }
}
