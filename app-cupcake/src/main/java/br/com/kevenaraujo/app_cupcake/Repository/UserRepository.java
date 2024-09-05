package br.com.kevenaraujo.app_cupcake.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.kevenaraujo.app_cupcake.Entity.Users;

public interface UserRepository extends JpaRepository<Users, String> {

    Optional<Users> findByEmail(String email);
    
}
