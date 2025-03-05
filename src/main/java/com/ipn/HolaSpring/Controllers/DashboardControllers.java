package com.ipn.HolaSpring.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ipn.HolaSpring.Models.User;
import com.ipn.HolaSpring.Services.UserService;

@Controller
public class DashboardControllers {
    
    @Autowired
    private UserService userService;
    
    // User dashboard
    @GetMapping("/user/dashboard")
    public String userDashboard(Model model, RedirectAttributes redirectAttributes) {
        try {
            // Obtener el usuario autenticado directamente de Spring Security
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            
            User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            model.addAttribute("user", user);
            
            return "user/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar el dashboard: " + e.getMessage());
            return "redirect:/login";
        }
    }
    
    // Admin dashboard
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model, RedirectAttributes redirectAttributes) {
        try {
            // Obtener el usuario autenticado directamente de Spring Security
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            
            if (!userService.isAdmin(email)) {
                return "redirect:/user/dashboard";
            }
            
            User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            model.addAttribute("user", user);
            
            return "admin/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar el dashboard: " + e.getMessage());
            return "redirect:/login";
        }
    }
}