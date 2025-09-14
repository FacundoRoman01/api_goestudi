package com.goestudi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para manejar casos en los que no se encuentra un perfil de empresa.
 * Anotada con @ResponseStatus para que Spring devuelva un 404 Not Found automáticamente.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CompanyProfileNotFoundException extends RuntimeException {

    public CompanyProfileNotFoundException(String message) {
        super(message);
    }
}
