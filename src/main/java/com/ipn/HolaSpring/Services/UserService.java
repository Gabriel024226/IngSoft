package com.ipn.HolaSpring.Services;

import com.ipn.HolaSpring.Models.Role;
import com.ipn.HolaSpring.Models.User;
import com.ipn.HolaSpring.Repositories.RoleRepository;
import com.ipn.HolaSpring.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    // Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Check if user exists by email
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    // Register a new user with USER role
    public User registerUser(User user) {
        // Assign USER role by default
        Role userRole = roleRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role USER not found"));
        user.addRole(userRole);
        
        return userRepository.save(user);
    }
    
    // Login validation
    public boolean validateLogin(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            // For this simple implementation, we're comparing passwords directly
            // In a real application, you would use password encryption
            return userOpt.get().getPassword().equals(password);
        }
        return false;
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