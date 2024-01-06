package br.com.gspadilha.gestaodevagas.module.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gspadilha.gestaodevagas.module.entities.JobEntity;
import br.com.gspadilha.gestaodevagas.module.repositories.JobRepository;

@Service
public class CreateJobUseCase {

    @Autowired
    private JobRepository jobRepository;

    public JobEntity execute(JobEntity jobEntity) {
        return this.jobRepository.save(jobEntity);
    }
}
