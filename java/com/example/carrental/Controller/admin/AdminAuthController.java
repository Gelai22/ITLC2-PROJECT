package com.example.carrental.Controller.admin;

import com.example.carrental.Models.admin.AdminModel;
import com.example.carrental.Services.admin.AdminAuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @GetMapping("/register")
    public String showAdminRegisterForm(Model model) {
        if (!model.containsAttribute("admin")) {
            model.addAttribute("admin", new AdminModel());
        }
        return "auth/register_admin";
    }

    @PostMapping("/register")
    public String processAdminRegistration(
            @Valid @ModelAttribute("admin") AdminModel admin,
            BindingResult bindingResult,
            @RequestParam("systemKey") String systemKey,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/register_admin";
        }

        if (!adminAuthService.validateSystemKey(systemKey)) {
            model.addAttribute("systemKeyError", "Invalid system key. Please try again.");
            return "auth/register_admin";
        }

        try {
            adminAuthService.registerNewAdmin(admin);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("emailExistsError", "The email address is already registered in the system.");
            return "redirect:/admin/register";
        }

        redirectAttributes.addFlashAttribute("registered", true);
        return "redirect:/admin/login";
    }

    @GetMapping("/login")
    public String adminLoginPage() {
        return "auth/login_admin";
    }

    @PostMapping("/login")
    public String processAdminLogin() {
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String adminLogout() {
        return "redirect:/";
    }
}