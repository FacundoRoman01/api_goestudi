package com.goestudi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para manejar casos en los que no se encuentra
 * un perfil de usuario.
 * <p>
 * Anotada con @ResponseStatus(HttpStatus.NOT_FOUND), lo que le indica a Spring
 * que debe responder con un código de estado HTTP 404 (Not Found)
 * cuando esta excepción sea lanzada.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserProfileNotFoundException extends RuntimeException {

    /**
     * Constructor que acepta un mensaje de error.
     * @param message El mensaje de error.
     */
    public UserProfileNotFoundException(String message) {
        super(message);
    }
}
