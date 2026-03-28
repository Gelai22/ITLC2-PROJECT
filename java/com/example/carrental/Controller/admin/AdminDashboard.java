package com.example.carrental.Controller.admin;

import com.example.carrental.Services.admin.DashboardService;
import com.example.carrental.Security.AdminPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboard {

    private final DashboardService dashboardService;

    public AdminDashboard(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping({"/dashboard"})
    public String dashboard(
            Model model,
            @AuthenticationPrincipal AdminPrincipal adminPrincipal
    ) {

        if (adminPrincipal != null) {
            model.addAttribute("adminUser", adminPrincipal.getAdmin());
        }

        // --- Core Metrics ---
        model.addAttribute("totalFleetCount", dashboardService.getTotalFleetCount());
        model.addAttribute("availableVehiclesCount", dashboardService.getAvailableVehiclesCount());
        model.addAttribute("inMaintenanceCount", dashboardService.getInMaintenanceCount());
        model.addAttribute("rentedOutCount", dashboardService.getRentedOutCount());
        model.addAttribute("pendingClientsCount", dashboardService.getPendingClientsCount());

        // --- Glance Bar Metrics ---
        model.addAttribute("activeRentalsCount", dashboardService.getActiveRentalCount());
        model.addAttribute("upcomingRentalsCount", dashboardService.getUpcomingRentalCount());
        model.addAttribute("overdueRentalsCount", dashboardService.getOverdueRentalCount());

        return "admin/dashboard";
    }
}