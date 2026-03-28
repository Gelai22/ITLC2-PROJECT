package com.example.carrental.Services.admin;

import com.example.carrental.Repositories.admin.MaintenanceRepository;
import com.example.carrental.Repositories.VehicleRepository;
import com.example.carrental.Repositories.client.ReservationRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final VehicleRepository vehicleRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final ReservationRepository reservationRepository;

    public DashboardService(VehicleRepository vehicleRepository, MaintenanceRepository maintenanceRepository, ReservationRepository reservationRepository) {
        this.vehicleRepository = vehicleRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.reservationRepository = reservationRepository;
    }

    public long getTotalFleetCount() {
        return vehicleRepository.count();
    }

    public long getAvailableVehiclesCount() {
        return vehicleRepository.countByStatus("AVAILABLE");
    }

    public long getRentedOutCount() {
        return vehicleRepository.countByStatus("RENTED");
    }

    public long getInMaintenanceCount() {
        return vehicleRepository.countByStatus("MAINTENANCE");
    }

    public long getPendingClientsCount() {
        return reservationRepository.countUniqueClientsByStatus("PENDING");
    }

    public long getActiveRentalCount() {
        return reservationRepository.countUniqueClientsByStatus("CONFIRMED");
    }

    public long getUpcomingRentalCount() {
        return 0;
    }

    public long getOverdueRentalCount() {
        return 0;
    }
}