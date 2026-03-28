package com.example.carrental.Controller.admin;

import com.example.carrental.Models.admin.MaintenanceLogViewModel;
import com.example.carrental.Models.admin.MaintenanceModel;
import com.example.carrental.Services.admin.MaintenanceService;
import com.example.carrental.Services.VehicleService;
import com.example.carrental.Security.AdminPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AdminMaintenanceLog {

    private final MaintenanceService maintenanceService;
    private final VehicleService vehicleService;

    public AdminMaintenanceLog(MaintenanceService maintenanceService, VehicleService vehicleService) {
        this.maintenanceService = maintenanceService;
        this.vehicleService = vehicleService;
    }

    @GetMapping({"/maintenance"})
    public String maintenance(
            Model model,
            @AuthenticationPrincipal AdminPrincipal adminPrincipal
    ) {
        if (adminPrincipal != null) {
            model.addAttribute("adminUser", adminPrincipal.getAdmin());
        }

        List<MaintenanceLogViewModel> activeLogs = maintenanceService.findActiveLogs();
        List<MaintenanceLogViewModel> completedLogs = maintenanceService.findCompletedLogs();

        model.addAttribute("activeLogs", activeLogs);
        model.addAttribute("completedLogs", completedLogs);
        model.addAttribute("activeMaintenanceCount", maintenanceService.getActiveCount());

        return "admin/maintenance_log";
    }

    @GetMapping({"/maintenance/new"})
    public String newMaintenance(
            Model model,
            @AuthenticationPrincipal AdminPrincipal adminPrincipal
    ){
        if (adminPrincipal != null) {
            model.addAttribute("adminUser", adminPrincipal.getAdmin());
        }

        model.addAttribute("maintenanceLog", new MaintenanceModel());
        model.addAttribute("vehicles", vehicleService.findAvailableForMaintenance());

        return "admin/new_maintenance";
    }

    @PostMapping("/maintenance/save")
    public String saveMaintenance(@ModelAttribute("maintenanceLog") MaintenanceModel log) {
        maintenanceService.saveNewLog(log);
        return "redirect:/maintenance";
    }

    @GetMapping("/maintenance/complete/{id}")
    public String completeMaintenanceAction(@PathVariable Long id) {
        maintenanceService.completeMaintenance(id);
        return "redirect:/maintenance";
    }
}