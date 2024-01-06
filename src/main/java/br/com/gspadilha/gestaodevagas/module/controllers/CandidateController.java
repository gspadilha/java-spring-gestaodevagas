package br.com.gspadilha.gestaodevagas.module.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gspadilha.gestaodevagas.module.entities.CandidateEntity;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @PostMapping("/")
    public void create(@Valid @RequestBody CandidateEntity candidateEntity) {
    }

    @PostMapping("/")
    public void read() {
    }

    @PostMapping("/")
    public void update() {
    }

    @PostMapping("/")
    public void delete() {
    }
}
