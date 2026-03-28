package com.example.carrental.Services.admin;

import com.example.carrental.Models.client.ReservationModel;
import com.example.carrental.Models.VehicleModel;
import com.example.carrental.Repositories.client.ReservationRepository;
import com.example.carrental.Services.VehicleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminReservationService {

    private final ReservationRepository reservationRepository;
    private final VehicleService vehicleService;

    public AdminReservationService(ReservationRepository reservationRepository, VehicleService vehicleService) {
        this.reservationRepository = reservationRepository;
        this.vehicleService = vehicleService;
    }

    public List<ReservationModel> getPendingApprovalReservations() {
        return reservationRepository.findByStatusWithDetails("PENDING");
    }

    public Long countPendingApprovalClients() {
        return reservationRepository.countUniqueClientsByStatus("PENDING");
    }

    public List<ReservationModel> getPendingReturnReservations() {
        return reservationRepository.findByStatusWithDetails("AWAITING_ADMIN_FINALIZATION");
    }

    public Optional<ReservationModel> findById(Long id) {
        return reservationRepository.findById(id);
    }

    @Transactional
    public ReservationModel updateReservation(ReservationModel reservation) {
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void updateVehicleStatus(VehicleModel vehicle, String newStatus) {
        vehicle.setStatus(newStatus);
        vehicleService.updateVehicle(vehicle);
    }

    public List<ReservationModel> getCompletedReservations() {
        return reservationRepository.findByStatusWithDetails("COMPLETED");
    }
}