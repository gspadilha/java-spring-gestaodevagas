package br.com.gspadilha.gestaodevagas.module.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gspadilha.gestaodevagas.module.entities.JobEntity;

public interface JobRepository extends JpaRepository<JobEntity, UUID> {
}
