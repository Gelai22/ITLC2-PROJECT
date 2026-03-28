package com.example.carrental.Controller.client;

import com.example.carrental.Dto.ReservationDTO;
import com.example.carrental.Models.client.ReservationModel;
import com.example.carrental.Models.VehicleModel;
import com.example.carrental.Models.client.ClientModel;
import com.example.carrental.Services.client.ReservationService;
import com.example.carrental.Services.VehicleService;
import com.example.carrental.Security.AdminPrincipal;
import com.example.carrental.Security.ClientPrincipal;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Controller
public class ClientAddReservation {

    private final ReservationService reservationService;
    private final VehicleService vehicleService;

    public ClientAddReservation(ReservationService reservationService, VehicleService vehicleService) {
        this.reservationService = reservationService;
        this.vehicleService = vehicleService;
    }

    @GetMapping({ "/reservation/add"})
    public String addReservation(
            Model model,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal,
            @RequestParam(required = false) Long vehicleId
    ) {
        if (clientPrincipal != null) {
            model.addAttribute("clientUser", clientPrincipal.getClient());
        }

        ReservationDTO dto = new ReservationDTO();

        if (vehicleId != null) {
            dto.setVehicleId(vehicleId);
        }

        if (vehicleId != null) {
            try {
                VehicleModel vehicle = vehicleService.findById(vehicleId);
                model.addAttribute("selectedVehicle", vehicle);
            } catch (IllegalArgumentException e) {
                return "redirect:/browse?error=vehicle_not_found";
            }
        }

        model.addAttribute("reservationDTO", dto);

        return "client/add_reservation";
    }

    @PostMapping("/reservation/create")
    public String createReservation(
            @Valid @ModelAttribute("reservationDTO") ReservationDTO dto,
            BindingResult bindingResult,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        ClientModel client = clientPrincipal != null ? clientPrincipal.getClient() : null;

        if (bindingResult.hasErrors()) {
            if (dto.getVehicleId() != null) {
                try {
                    VehicleModel vehicle = vehicleService.findById(dto.getVehicleId());
                    model.addAttribute("selectedVehicle", vehicle);
                } catch (IllegalArgumentException e) {
                    redirectAttributes.addFlashAttribute("errorMessage", "The selected vehicle was not found.");
                    return "redirect:/browse";
                }
            } else {
                model.addAttribute("errorMessage", "Please select a valid vehicle before proceeding.");
            }

            if (clientPrincipal != null) {
                model.addAttribute("clientUser", clientPrincipal.getClient());
            }

            model.addAttribute("reservationDTO", dto);
            return "client/add_reservation";
        }

        if (client == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Authentication failed. Please log in to complete the reservation.");
            return "redirect:/browse";
        }

        VehicleModel vehicle = vehicleService.findById(dto.getVehicleId());
        BigDecimal dailyRate = vehicle.getRentalPrice();

        LocalDateTime pickupDateTime = dto.getPickupDate().atStartOfDay();
        LocalDateTime returnDateTime = dto.getReturnDate().atStartOfDay();

        long days = ChronoUnit.DAYS.between(pickupDateTime, returnDateTime);

        if (days < 1) {
            redirectAttributes.addFlashAttribute("errorMessage", "Rental period must be at least one day and the return date must be after the pickup date.");
            return "redirect:/reservation/add?vehicleId=" + vehicle.getId();
        }

        BigDecimal totalCost = dailyRate.multiply(BigDecimal.valueOf(days));

        ReservationModel reservation = new ReservationModel();
        reservation.setClient(client);
        reservation.setVehicle(vehicle);
        reservation.setPickupDate(pickupDateTime);
        reservation.setReturnDate(returnDateTime);
        reservation.setTotalCost(totalCost);
        reservation.setPickupLocation(dto.getPickupLocation());
        reservation.setClientFeedback(dto.getReturnLocation());

        ReservationModel savedReservation = reservationService.createReservation(reservation);

        redirectAttributes.addFlashAttribute("successMessage", "Reservation draft created successfully. Awaiting administrator review.");
        return "redirect:/reservation";
    }


    @GetMapping("/reservation/confirm/{id}")
    public String showConfirmationPage(
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal
    ) {
        if (clientPrincipal != null) {
            model.addAttribute("clientUser", clientPrincipal.getClient());
        }

        ReservationModel reservation = reservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID: " + id));

        model.addAttribute("reservation", reservation);

        return "client/reservation_confirmation";
    }

    @PostMapping("/reservation/pay/{id}")
    public String processPaymentAndReceipt(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ReservationModel reservation = reservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID: " + id));

        reservation.setStatus("CONFIRMED");
        reservation.setPaymentStatus("PAID");
        reservation.setPaymentDate(reservation.getReservationDate().toLocalDate());

        ReservationModel paidReservation = reservationService.createReservation(reservation);

        VehicleModel vehicle = paidReservation.getVehicle();
        if (vehicle != null) {
            vehicle.setStatus("RENTED");
            vehicleService.updateVehicle(vehicle);
        }

        long days = ChronoUnit.DAYS.between(paidReservation.getPickupDate(), paidReservation.getReturnDate());
        redirectAttributes.addFlashAttribute("rentalDays", days);

        return "redirect:/reservation/receipt/" + paidReservation.getId();
    }


    @GetMapping("/reservation/receipt/{id}")
    public String showReceipt(
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal UserDetails principal
    ) {
        boolean isAdminView = false;

        if (principal instanceof AdminPrincipal adminPrincipal) {
            model.addAttribute("adminUser", adminPrincipal.getAdmin());
            isAdminView = true;
        } else if (principal instanceof ClientPrincipal clientPrincipal) {
            model.addAttribute("clientUser", clientPrincipal.getClient());
        }

        model.addAttribute("isAdminView", isAdminView);

        ReservationModel reservation = reservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID: " + id));

        model.addAttribute("reservation", reservation);

        long days = ChronoUnit.DAYS.between(reservation.getPickupDate(), reservation.getReturnDate());
        model.addAttribute("rentalDays", days);

        return "client/reservation_receipt";
    }

    @GetMapping("/reservation/return/{id}")
    public String initiateReturnAndCompensation(
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal
    ) {
        if (clientPrincipal != null) {
            model.addAttribute("clientUser", clientPrincipal.getClient());
        }

        ReservationModel reservation = reservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID: " + id));

        reservation.setStatus("AWAITING_ADMIN_FINALIZATION");
        reservationService.createReservation(reservation);

        model.addAttribute("reservation", reservation);
        return "client/vehicle_review";
    }
}