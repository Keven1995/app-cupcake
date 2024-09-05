package br.com.kevenaraujo.app_cupcake.Infra;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.kevenaraujo.app_cupcake.Entity.Users;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Users users) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

            String token = JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(users.getEmail())
                    .withExpiresAt(this.generateExperationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error of the authentication");
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            return JWT.require(algorithm)
            .withIssuer("login-auth-api")
            .build()
            .verify(token)
            .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant generateExperationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
