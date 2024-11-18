package co.luna.simple.jwt.auth.controllers;


import co.luna.simple.jwt.auth.controllers.custom.requests.LogInRequest;
import co.luna.simple.jwt.auth.controllers.custom.response.TokenResponse;
import co.luna.simple.jwt.auth.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> logInUsuario(@RequestBody @Valid LogInRequest logInRequest){
        return ResponseEntity.ok(authService.logInUsuario(logInRequest));
    }
    @GetMapping("/test")
    public ResponseEntity<String> test(@AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok(user.toString());
    }
}
