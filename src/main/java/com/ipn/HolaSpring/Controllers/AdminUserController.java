package com.ipn.HolaSpring.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ipn.HolaSpring.Models.User;
import com.ipn.HolaSpring.Services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminUserController {
    
    @Autowired
    private UserService userService;
    
    // List all users
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users";
    }
    
    // Show form to create new user
    @GetMapping("/users/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user-form";
    }
    
    // Create new user
    @PostMapping("/users/new")
    public String createUser(@ModelAttribute User user, 
                             @RequestParam(required = false) boolean isAdmin, 
                             RedirectAttributes redirectAttributes) {
        try {
            if (userService.existsByEmail(user.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "El correo electrónico ya está registrado");
                return "redirect:/admin/users/new";
            }
            
            if (isAdmin) {
                userService.registerAdmin(user);
            } else {
                userService.registerUser(user);
            }
            
            redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear usuario: " + e.getMessage());
            return "redirect:/admin/users/new";
        }
    }
    
    // Show form to edit user
    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
            
            model.addAttribute("user", user);
            model.addAttribute("isAdmin", userService.isAdmin(user.getEmail()));
            
            return "admin/user-edit-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar usuario: " + e.getMessage());
            return "redirect:/admin/users";
        }
    }
    
    // Update user
    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id, 
                            @ModelAttribute User user,
                            @RequestParam(required = false) boolean isAdmin,
                            RedirectAttributes redirectAttributes) {
        try {
            User existingUser = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
            
            // Actualizar datos básicos
            existingUser.setNombre(user.getNombre());
            existingUser.setEmail(user.getEmail());
            
            // Actualizar contraseña solo si se proporciona
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(user.getPassword());
            }
            
            // Actualizar roles según la casilla de verificación
            boolean wasAdmin = userService.isAdmin(existingUser.getEmail());
            if (isAdmin && !wasAdmin) {
                userService.makeUserAdmin(existingUser);
            } else if (!isAdmin && wasAdmin) {
                userService.removeAdminRole(existingUser);
            }
            
            userService.updateUser(existingUser);
            
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar usuario: " + e.getMessage());
            return "redirect:/admin/users/edit/" + id;
        }
    }
    
    // Delete user
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar usuario: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}