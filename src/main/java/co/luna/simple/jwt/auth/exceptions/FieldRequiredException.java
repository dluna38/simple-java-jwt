package co.luna.simple.jwt.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class FieldRequiredException extends AppHttpException{
    private final String[] fieldsMissing;

    public FieldRequiredException(String... fieldMissing){
        super("Campos Faltantes",HttpStatus.BAD_REQUEST);
        this.fieldsMissing = fieldMissing;
    }

    @Override
    protected ResponseEntity<Object> generateResponse() {
        if(fieldsMissing.length != 0){
            addToExtraContent(Map.of("campos_faltantes",String.join(", ",this.fieldsMissing)));
        }
        return super.generateResponse();
    }
}
