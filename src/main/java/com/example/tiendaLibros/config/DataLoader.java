package com.example.tiendaLibros.config;

import com.example.tiendaLibros.usuario.Rol;
import com.example.tiendaLibros.usuario.RolRepository;
import com.example.tiendaLibros.usuario.Usuario;
import com.example.tiendaLibros.usuario.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository,
                                   RolRepository rolRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                // Crear
                Rol rolLector = rolRepository.findByNombre("ROLE_LECTOR")
                        .orElseGet(() -> rolRepository.save(new Rol(null, "ROLE_LECTOR")));
                Rol rolEditor = rolRepository.findByNombre("ROLE_EDITOR")
                        .orElseGet(() -> rolRepository.save(new Rol(null, "ROLE_EDITOR")));
                Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
                        .orElseGet(() -> rolRepository.save(new Rol(null, "ROLE_ADMIN")));

                // Crear usuarios por defecto
                Usuario lector = new Usuario(null, "lector", passwordEncoder.encode("lector123"), true, Set.of(rolLector));
                Usuario editor = new Usuario(null, "editor", passwordEncoder.encode("editor123"), true, Set.of(rolLector, rolEditor));
                Usuario admin = new Usuario(null, "admin", passwordEncoder.encode("admin123"), true, Set.of(rolLector, rolEditor, rolAdmin));

                usuarioRepository.saveAll(Set.of(lector, editor, admin));
            }
        };
    }
}