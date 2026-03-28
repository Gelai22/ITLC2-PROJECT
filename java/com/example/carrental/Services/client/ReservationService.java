package com.example.carrental.Services.client;

import com.example.carrental.Models.client.ReservationModel;
import com.example.carrental.Models.VehicleModel;
import com.example.carrental.Models.client.ReservationLogViewModel;
import com.example.carrental.Repositories.client.ReservationRepository;
import com.example.carrental.Services.VehicleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final VehicleService vehicleService;

    public ReservationService(ReservationRepository reservationRepository, VehicleService vehicleService) {
        this.reservationRepository = reservationRepository;
        this.vehicleService = vehicleService;
    }

    @Transactional
    public ReservationModel createReservation(ReservationModel reservation) {
        return reservationRepository.save(reservation);
    }

    public Optional<ReservationModel> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public List<ReservationLogViewModel> getPendingReservations(Long clientId) {
        List<String> activeStatuses = Arrays.asList("PENDING", "PENDING_PAYMENT", "CONFIRMED");
        List<ReservationModel> reservations = reservationRepository.findByClientIdAndStatusIn(clientId, activeStatuses);

        return reservations.stream()
                .map(res -> {
                    VehicleModel vehicle = vehicleService.findById(res.getVehicle().getId());

                    return new ReservationLogViewModel(
                            res,
                            vehicle.getVehicleModel(),
                            vehicle.getYear(),
                            vehicle.getPlateNumber()
                    );
                })
                .collect(Collectors.toList());
    }

    public List<ReservationLogViewModel> getHistoryReservations(Long clientId) {
        List<ReservationModel> reservations = reservationRepository.findByClientIdAndStatus(clientId, "COMPLETED");

        return reservations.stream()
                .map(res -> {
                    VehicleModel vehicle = vehicleService.findById(res.getVehicle().getId());

                    return new ReservationLogViewModel(
                            res,
                            vehicle.getVehicleModel(),
                            vehicle.getYear(),
                            vehicle.getPlateNumber()
                    );
                })
                .collect(Collectors.toList());
    }
}