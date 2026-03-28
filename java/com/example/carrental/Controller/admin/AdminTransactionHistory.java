package com.example.carrental.Controller.admin;

import com.example.carrental.Models.client.ReservationModel;
import com.example.carrental.Services.admin.AdminReservationService;
import com.example.carrental.Security.AdminPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminTransactionHistory {

    private final AdminReservationService adminReservationService;

    public AdminTransactionHistory(AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @GetMapping("/rental/history")
    public String rentalHistory(
            Model model,
            @AuthenticationPrincipal AdminPrincipal adminPrincipal
    ) {
        if (adminPrincipal != null) {
            model.addAttribute("adminUser", adminPrincipal.getAdmin());
        }

        List<ReservationModel> completedReservations = adminReservationService.getCompletedReservations();
        model.addAttribute("completedReservations", completedReservations);

        return "admin/transaction_history";
    }
}