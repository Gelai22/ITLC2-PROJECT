package com.example.carrental.Controller.client;

import com.example.carrental.Models.client.ReservationLogViewModel;
import com.example.carrental.Services.client.ReservationService;
import com.example.carrental.Security.ClientPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ClientReservationHistory {

    private final ReservationService reservationService;

    public ClientReservationHistory(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping({"/reservation/history"})
    public String reservationHistory(
            Model model,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal
    ) {
        if (clientPrincipal == null) {
            return "redirect:/login";
        }

        model.addAttribute("clientUser", clientPrincipal.getClient());

        Long clientId = clientPrincipal.getClient().getId();

        List<ReservationLogViewModel> historyReservations = reservationService.getHistoryReservations(clientId);
        model.addAttribute("historyReservations", historyReservations);

        List<ReservationLogViewModel> pendingReservations = reservationService.getPendingReservations(clientId);
        model.addAttribute("pendingCount", pendingReservations.size());

        return "client/rental_history";
    }
}