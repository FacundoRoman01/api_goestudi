package com.goestudi.exception;

/**
 * Excepción personalizada que se lanza cuando un perfil de usuario ya existe.
 */
public class UserProfileAlreadyExistsException extends RuntimeException {

    public UserProfileAlreadyExistsException(String message) {
        super(message);
    }
}
