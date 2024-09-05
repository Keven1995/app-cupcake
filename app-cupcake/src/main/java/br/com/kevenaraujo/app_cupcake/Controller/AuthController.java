package br.com.kevenaraujo.app_cupcake.Controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.kevenaraujo.app_cupcake.DTO.LoginRequestDTO;
import br.com.kevenaraujo.app_cupcake.DTO.RegisterRequestDTO;
import br.com.kevenaraujo.app_cupcake.DTO.ResponseDTO;
import br.com.kevenaraujo.app_cupcake.Entity.Users;
import br.com.kevenaraujo.app_cupcake.Infra.TokenService;
import br.com.kevenaraujo.app_cupcake.Repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository repository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {
        Users users = this.repository.findByEmail(body.email())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (passwordEncoder.matches(body.password(), users.getPassword())) {
            String token = this.tokenService.generateToken(users);
            return ResponseEntity.ok(new ResponseDTO(users.getName(), token));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
        Optional<Users> users = this.repository.findByEmail(body.email());
        if (users.isEmpty()) {

            Users newUsers = new Users();
            newUsers.setPassword(passwordEncoder.encode(body.password()));
            newUsers.setName(body.email());
            newUsers.setName(body.name());
            this.repository.save(newUsers);

            String token = this.tokenService.generateToken(newUsers);
            return ResponseEntity.ok(new ResponseDTO(newUsers.getName(), token));
        }

        return ResponseEntity.badRequest().build();
    }
}
