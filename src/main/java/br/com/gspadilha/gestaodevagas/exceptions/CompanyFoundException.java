package br.com.gspadilha.gestaodevagas.exceptions;

public class CompanyFoundException extends RuntimeException {
    public CompanyFoundException() {
        super("Empresa já cadastrado!");
    }
}
