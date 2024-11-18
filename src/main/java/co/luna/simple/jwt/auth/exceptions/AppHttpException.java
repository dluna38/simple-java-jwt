package co.luna.simple.jwt.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AppHttpException extends RuntimeException{
    protected final HttpStatus statusCode;
    protected final transient  Map<String,Object> responseBody;
    protected final transient List<Map<String,Object>> extraContent;
    protected AppHttpException(String message,HttpStatus statusCode) {
        super(message);
        this.statusCode=statusCode;
        this.responseBody = new HashMap<>();
        this.extraContent = new ArrayList<>();
        this.responseBody.put("code",statusCode.value());
        this.responseBody.put("msg",message);
        this.responseBody.put("extra",this.extraContent);
    }

    protected ResponseEntity<Object> generateResponse(){
        return new ResponseEntity<>(responseBody,statusCode);
    }

    protected void addToExtraContent(Map<String,Object> extraContent){
        this.extraContent.add(extraContent);
    }

}
