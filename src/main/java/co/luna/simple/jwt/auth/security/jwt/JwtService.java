package co.luna.simple.jwt.auth.security.jwt;

import co.luna.simple.jwt.auth.exceptions.ValidationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    private final long ExpirationTime = 1000L*60*60*24;

    @Value("${JWT_KEY}")
    private String signKey;
    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }
    protected String extractUsername(Claims claims) {
        return claims.getSubject();
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    protected Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSignInKey() {
        if(signKey == null) throw new ValidationException("key jwt","jwt_key missing");
        byte[] keyBytes = Decoders.BASE64.decode(signKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){
        return Jwts.builder().subject(userDetails.getUsername()).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ExpirationTime))
                .claim("roles",userDetails.getAuthorities().toString())
                .claims(extraClaims)
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();

    }

    public String generateToken(UserDetails userDetails){
        return generateToken(Collections.emptyMap(),userDetails);
    }

    public boolean validateTokenFull(String token,UserDetails userDetails){
        return isTokenExpired(token) && checkUser(token,userDetails);
    }

    /**
     * check expiration and the user roles and username
     * @param claims
     * @param userDetails
     * @return
     */
    protected boolean validateTokenFull(Claims claims,UserDetails userDetails){
        return !isTokenExpired(claims) && checkUser(claims,userDetails);
    }
    public boolean validateTokenSimple(String token){
        return !isTokenExpired(token);
    }
    private boolean checkUser(String token,UserDetails userDetails){
        Claims claims = extractAllClaims(token);
        try {
            return claims.getSubject().equals(userDetails.getUsername()) && claims.get("roles").equals(userDetails.getAuthorities().toString());
        } catch (Exception e) {
            return false;
        }
    }
    private boolean checkUser(Claims claims,UserDetails userDetails){
        try {
            return claims.getSubject().equals(userDetails.getUsername()) && claims.get("roles").equals(userDetails.getAuthorities().toString());
        } catch (Exception e) {
            return false;
        }
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }


}

