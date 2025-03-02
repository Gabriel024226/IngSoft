package com.ipn.HolaSpring.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ipn.HolaSpring.Services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardControllers {
    
    @Autowired
    private UserService userService;
    
    // User dashboard
    @GetMapping("/user/dashboard")
    public String userDashboard(HttpSession session, Model model) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null) {
            return "redirect:/login";
        }
        
        userService.findByEmail(email).ifPresent(user -> {
            model.addAttribute("user", user);
        });
        
        return "user/dashboard";
    }
    
    // Admin dashboard
    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        String email = (String) session.getAttribute("userEmail");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        
        if (email == null || isAdmin == null || !isAdmin) {
            return "redirect:/login";
        }
        
        userService.findByEmail(email).ifPresent(user -> {
            model.addAttribute("user", user);
        });
        
        return "admin/dashboard";
    }
}