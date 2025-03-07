package com.ipn.HolaSpring.Services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ipn.HolaSpring.Models.Role;
import com.ipn.HolaSpring.Models.User;
import com.ipn.HolaSpring.Repositories.RoleRepository;
import com.ipn.HolaSpring.Repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Find all users
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    // Find user by ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    // Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Check if user exists by email
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    // Register a new user with USER role
    @Transactional
    public User registerUser(User user) {
        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Assign USER role by default
        Role userRole = roleRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role USER not found"));
        
        user.addRole(userRole);
        
        return userRepository.save(user);
    }
    
    // Register admin user
    @Transactional
    public User registerAdmin(User user) {
        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Assign ADMIN role
        Role adminRole = roleRepository.findByNombre("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Error: Role ADMIN not found"));
        user.addRole(adminRole);
        
        // También asignar el rol USER
        Role userRole = roleRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role USER not found"));
        user.addRole(userRole);
        
        return userRepository.save(user);
    }
    
    // Update user (sin modificar roles)
    @Transactional
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + user.getId()));
        
        existingUser.setNombre(user.getNombre());
        existingUser.setEmail(user.getEmail());
        
        // Solo actualizar la contraseña si se proporciona
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        return userRepository.save(existingUser);
    }
    
    // Hacer a un usuario administrador
    @Transactional
    public void makeUserAdmin(User user) {
        Role adminRole = roleRepository.findByNombre("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Error: Role ADMIN not found"));
        
        user.addRole(adminRole);
        userRepository.save(user);
    }
    
    // Quitar rol de administrador
    @Transactional
    public void removeAdminRole(User user) {
        Role adminRole = roleRepository.findByNombre("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Error: Role ADMIN not found"));
        
        user.getRoles().removeIf(role -> role.getNombre().equals("ROLE_ADMIN"));
        userRepository.save(user);
    }
    
    // Delete user
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    // Get user roles
    public Set<Role> getUserRoles(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.map(User::getRoles).orElse(new HashSet<>());
    }
    
    // Check if user has admin role
    public boolean isAdmin(String email) {
        Set<Role> roles = getUserRoles(email);
        return roles.stream()
                .anyMatch(role -> role.getNombre().equals("ROLE_ADMIN"));
    }
}