package co.luna.simple.jwt.auth.exceptions;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ValidationException extends AppHttpException{
    public ValidationException(String campo, String error) {
        super("Error en la validación de datos", HttpStatus.BAD_REQUEST);
        addToExtraContent(Map.of("campo",campo,"error",error));
    }
    public ValidationException() {
        super("Error en la validación de datos", HttpStatus.BAD_REQUEST);
    }
}
