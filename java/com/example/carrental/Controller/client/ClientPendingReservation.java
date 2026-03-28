package com.example.carrental.Controller.client;

import com.example.carrental.Models.client.ClientModel;
import com.example.carrental.Models.client.ReservationLogViewModel;
import com.example.carrental.Services.client.ReservationService;
import com.example.carrental.Security.ClientPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ClientPendingReservation {

    private final ReservationService reservationService;

    public ClientPendingReservation(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping({"/reservation"})
    public String pendingReservations(
            Model model,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal
    ) {
        if (clientPrincipal == null) {
            return "redirect:/login";
        }

        model.addAttribute("clientUser", clientPrincipal.getClient());

        ClientModel client = clientPrincipal.getClient();
        Long clientId = client.getId();

        List<ReservationLogViewModel> allActiveLogs = reservationService.getPendingReservations(clientId);

        List<ReservationLogViewModel> pendingApproval = allActiveLogs.stream()
                .filter(res -> "PENDING".equals(res.getStatus()) || "PENDING_PAYMENT".equals(res.getStatus()))
                .collect(Collectors.toList());

        List<ReservationLogViewModel> confirmedOngoing = allActiveLogs.stream()
                .filter(res -> "CONFIRMED".equals(res.getStatus()))
                .collect(Collectors.toList());

        model.addAttribute("pendingApproval", pendingApproval);
        model.addAttribute("confirmedOngoing", confirmedOngoing);
        model.addAttribute("pendingReservations", allActiveLogs);

        List<ReservationLogViewModel> historyLogs = reservationService.getHistoryReservations(clientId);
        model.addAttribute("historyCount", historyLogs.size());

        return "client/pending_reservation";
    }
}