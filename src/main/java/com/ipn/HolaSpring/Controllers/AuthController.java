package com.ipn.HolaSpring.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ipn.HolaSpring.Models.User;
import com.ipn.HolaSpring.Services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    // Show registration form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    // Process registration
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        // Check if user already exists
        if (userService.existsByEmail(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "El correo electrónico ya está registrado");
            return "redirect:/register";
        }
        
        try {
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "¡Registro exitoso! Ahora puedes iniciar sesión.");
            return "redirect:/register?success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error durante el registro: " + e.getMessage());
            return "redirect:/register";
        }
    }
    
    // Show login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    
    // Process login
    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password, 
                              HttpSession session, RedirectAttributes redirectAttributes) {
        if (userService.validateLogin(email, password)) {
            // Store user information in session
            session.setAttribute("userEmail", email);
            session.setAttribute("isLoggedIn", true);
            
            // Check if user is admin
            boolean isAdmin = userService.isAdmin(email);
            session.setAttribute("isAdmin", isAdmin);
            
            // Redirect based on role
            if (isAdmin) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/user/dashboard";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Correo electrónico o contraseña incorrectos");
            return "redirect:/login";
        }
    }
    
    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    // Home page - redirects to registration
    @GetMapping("/")
    public String home() {
        return "redirect:/register";
    }
}