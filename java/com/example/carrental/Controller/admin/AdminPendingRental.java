package com.example.carrental.Controller.admin;

import com.example.carrental.Models.client.ReservationModel;
import com.example.carrental.Models.VehicleModel;
import com.example.carrental.Services.admin.AdminReservationService;
import com.example.carrental.Services.VehicleService;
import com.example.carrental.Security.AdminPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminPendingRental {

    private final AdminReservationService adminReservationService;
    private final VehicleService vehicleService;

    public AdminPendingRental(AdminReservationService adminReservationService, VehicleService vehicleService) {
        this.adminReservationService = adminReservationService;
        this.vehicleService = vehicleService;
    }

    @GetMapping({"/rental"})
    public String viewPendingRentals(
            Model model,
            @AuthenticationPrincipal AdminPrincipal adminPrincipal // Inject principal
    ) {
        // --- Add Admin User to Model ---
        if (adminPrincipal != null) {
            model.addAttribute("adminUser", adminPrincipal.getAdmin());
        }

        List<ReservationModel> pendingReservations = adminReservationService.getPendingApprovalReservations();
        List<ReservationModel> pendingReturns = adminReservationService.getPendingReturnReservations();

        model.addAttribute("pendingReservations", pendingReservations);
        model.addAttribute("pendingReturns", pendingReturns);
        model.addAttribute("totalPendingCount", pendingReservations.size());

        return "admin/pending_rental";
    }

    @PostMapping("/rental/approve/{id}")
    public String approveReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ReservationModel reservation = adminReservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID: " + id));

        if (!"PENDING".equals(reservation.getStatus())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Reservation must be in PENDING status to be approved.");
            return "redirect:/rental";
        }

        reservation.setStatus("PENDING_PAYMENT");
        adminReservationService.updateReservation(reservation);

        redirectAttributes.addFlashAttribute("successMessage", "Reservation #R" + id + " approved. Awaiting client payment.");
        return "redirect:/rental";
    }

    @PostMapping("/reservation/submit-return-review/{id}")
    public String submitClientReturnReview(
            @PathVariable Long id,
            @RequestParam(required = false) String rentalRating,
            @RequestParam(required = false) String finalNotes,
            RedirectAttributes redirectAttributes) {

        ReservationModel reservation = adminReservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID: " + id));

        reservation.setStatus("AWAITING_ADMIN_FINALIZATION");
        reservation.setRating(rentalRating);
        reservation.setClientFeedback(finalNotes);

        adminReservationService.updateReservation(reservation);

        redirectAttributes.addFlashAttribute("successMessage", "Return review submitted. Administrator will review final costs and close the rental shortly.");
        return "redirect:/reservation";
    }


    @PostMapping("/rental/return/{id}")
    public String processReturn(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ReservationModel reservation = adminReservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID: " + id));

        if (!"AWAITING_ADMIN_FINALIZATION".equals(reservation.getStatus())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot finalize rental. Client must submit return review first.");
            return "redirect:/rental";
        }

        reservation.setStatus("COMPLETED");

        adminReservationService.updateReservation(reservation);

        VehicleModel vehicle = reservation.getVehicle();
        if (vehicle != null) {
            vehicle.setStatus("AVAILABLE");
            vehicleService.updateVehicle(vehicle);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Rental #R" + id + " successfully processed and moved to history.");
        return "redirect:/rental/history";
    }
}