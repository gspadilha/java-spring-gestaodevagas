package br.com.gspadilha.gestaodevagas.module.entities;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CandidateEntity {
    private UUID id;
    private String name;
    @Pattern(regexp = "\\S+", message = "O campo [username] não deve conter espaços!")
    private String username;
    @Length(min = 8, max = 100, message = "O campo [password] deve conter entre 8 e 100 caracteres!")
    private String password;
    @Email(message = "O campo [email] deve conter um e-mail válido!")
    private String email;
    private String description;
    private String curriculum;
}
