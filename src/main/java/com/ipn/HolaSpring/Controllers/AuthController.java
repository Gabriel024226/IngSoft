package com.ipn.HolaSpring.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error durante el registro: " + e.getMessage());
            return "redirect:/register";
        }
    }
    
    // Show login form
    @GetMapping("/login")
    public String showLoginForm() {
        // Spring Security will handle the login form submission
        return "login";
    }
    
    // Home page after login - redirects based on role
    @GetMapping("/home")
    public String home(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        // Guardar el email en la sesión
        session.setAttribute("userEmail", email);
        
        // Verificar si es admin y guardar en sesión
        boolean isAdmin = userService.isAdmin(email);
        session.setAttribute("isAdmin", isAdmin);
        
        if (isAdmin) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/user/dashboard";
        }
    }
    
    // Root path redirects to home for authenticated users, or login for others
    @GetMapping("/")
    public String root() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && 
            !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return "redirect:/home";
        }
        return "redirect:/login";
    }
}