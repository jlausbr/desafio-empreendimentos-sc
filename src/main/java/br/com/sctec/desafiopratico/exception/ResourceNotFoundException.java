package br.com.sctec.desafiopratico.exception;

/**
 * Exceção lançada quando um recurso solicitado não é encontrado.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
