package co.luna.simple.jwt.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllersExceptionHandler {

    @ExceptionHandler(FieldRequiredException.class)
    public ResponseEntity<Object> fieldRequiredResponse(FieldRequiredException ex) {
        return ex.generateResponse();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundResponse(ResourceNotFoundException ex){
        return ex.generateResponse();
    }
    @ExceptionHandler(UnknownException.class)
    public ResponseEntity<Object> unknownExceptionResponse(ResourceNotFoundException ex){
        return ex.generateResponse();
    }
    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<Object> validationExceptionResponse(ValidationException ex){
        System.out.println(ex.extraContent.get(0));
        return ex.generateResponse();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> authenticationExceptionResponse(AuthenticationException ex){

        return ResponseEntity.badRequest().body("No se pudo autenticar - revisar credenciales");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidExceptionResponse(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach( error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

   /* @ExceptionHandler(SqlEx.class)
    public ResponseEntity<Object> sqlExceptionResponse(PSQLException ex){
        return new DatabaseHandleExceptions(ex).generateResponse();
    }*/

}
