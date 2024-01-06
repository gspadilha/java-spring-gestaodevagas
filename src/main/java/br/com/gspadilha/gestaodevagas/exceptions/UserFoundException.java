package br.com.gspadilha.gestaodevagas.exceptions;

public class UserFoundException extends RuntimeException {
    public UserFoundException() {
        super("Usuário já cadastrado!");
    }
}
