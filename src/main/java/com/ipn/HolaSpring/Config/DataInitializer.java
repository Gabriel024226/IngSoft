package com.ipn.HolaSpring.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ipn.HolaSpring.Models.Role;
import com.ipn.HolaSpring.Models.User;
import com.ipn.HolaSpring.Repositories.RoleRepository;
import com.ipn.HolaSpring.Repositories.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Inicializar roles si no existen
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("ROLE_USER"));
            roleRepository.save(new Role("ROLE_ADMIN"));
            System.out.println("Roles creados correctamente.");
        }
        
        // Crear usuario administrador por defecto si no existe
        if (!userRepository.existsByEmail("admin@sistema.com")) {
            User adminUser = new User();
            adminUser.setNombre("Administrador");
            adminUser.setEmail("admin@sistema.com");
            // Encriptar la contraseña directamente aquí
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            
            // Asignar roles manualmente
            Role adminRole = roleRepository.findByNombre("ROLE_ADMIN").orElseThrow();
            Role userRole = roleRepository.findByNombre("ROLE_USER").orElseThrow();
            adminUser.addRole(adminRole);
            adminUser.addRole(userRole);
            
            userRepository.save(adminUser);
            System.out.println("Usuario administrador creado correctamente.");
        }
        
        // Crear usuario normal por defecto si no existe
        if (!userRepository.existsByEmail("user@sistema.com")) {
            User regularUser = new User();
            regularUser.setNombre("Usuario");
            regularUser.setEmail("user@sistema.com");
            // Encriptar la contraseña directamente aquí
            regularUser.setPassword(passwordEncoder.encode("user123"));
            
            // Asignar rol manualmente
            Role userRole = roleRepository.findByNombre("ROLE_USER").orElseThrow();
            regularUser.addRole(userRole);
            
            userRepository.save(regularUser);
            System.out.println("Usuario regular creado correctamente.");
        }
    }
}