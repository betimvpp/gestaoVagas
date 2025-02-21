package br.com.gestaodevagas.gestao_de_vagas.exceptions;

public class UserFoundException extends RuntimeException {

    public UserFoundException() {
        super("User already exists");
    }
}
