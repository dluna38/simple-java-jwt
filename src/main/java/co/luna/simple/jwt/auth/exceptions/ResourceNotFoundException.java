package co.luna.simple.jwt.auth.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AppHttpException{
    public ResourceNotFoundException() {
        super("No se encontró el recurso", HttpStatus.NOT_FOUND);
    }
    public ResourceNotFoundException(String message) {
        super("No se encontró el recurso: "+message, HttpStatus.NOT_FOUND);
    }
}
