package br.com.kevenaraujo.app_cupcake.Infra;

import br.com.kevenaraujo.app_cupcake.Repository.UserRepository;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.kevenaraujo.app_cupcake.Entity.Users;
@Component
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = this.repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        return new org.springframework.security.core.userdetails.User(users.getEmail(), users.getPassword(), Collections.singletonList(authority));
    }
}
