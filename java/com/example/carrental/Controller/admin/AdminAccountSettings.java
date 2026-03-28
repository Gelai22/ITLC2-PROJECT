package com.example.carrental.Controller.admin;

import com.example.carrental.Models.admin.AdminModel;
import com.example.carrental.Repositories.admin.AdminRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminAccountSettings {

    private final AdminRepository adminRepository;

    public AdminAccountSettings(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @GetMapping("/account")
    public String account(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        AdminModel admin = adminRepository.findByEmailAddress(username).orElse(null);

        if (admin != null) {
            model.addAttribute("admin", admin);

            String employeeName = admin.getEmployeeName();
            String displayUserName = (employeeName != null && !employeeName.isEmpty()) ? employeeName : username;

            model.addAttribute("headerUserName", displayUserName);
            model.addAttribute("headerUserEmail", username);

            return "admin_ccount_settings";
        }

        return "redirect:/dashboard";
    }
}