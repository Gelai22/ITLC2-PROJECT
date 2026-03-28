package com.example.carrental.Services.admin;

import com.example.carrental.Models.admin.MaintenanceLogViewModel;
import com.example.carrental.Models.admin.MaintenanceModel;
import com.example.carrental.Models.VehicleModel;
import com.example.carrental.Repositories.admin.MaintenanceRepository;
import com.example.carrental.Services.VehicleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehicleService vehicleService; // Inject VehicleService to update car status

    public MaintenanceService(MaintenanceRepository maintenanceRepository, VehicleService vehicleService) {
        this.maintenanceRepository = maintenanceRepository;
        this.vehicleService = vehicleService;
    }

    /**
     * Saves a new maintenance log (starts service) and sets the linked vehicle to MAINTENANCE status.
     */
    @Transactional
    public MaintenanceModel saveNewLog(MaintenanceModel log) {

        // 1. Set start date and status
        log.setDateOut(LocalDate.now());
        log.setStatus("IN_PROGRESS");

        // 2. Update the vehicle's status (using the injected VehicleService)
        if (log.getVehicleId() != null) {
            VehicleModel vehicle = vehicleService.findById(log.getVehicleId());
            vehicle.setStatus("MAINTENANCE");
            vehicleService.saveVehicle(vehicle); // Persist the change via VehicleService
        }

        // 3. Save the maintenance log
        return maintenanceRepository.save(log);
    }

    public List<MaintenanceLogViewModel> findCompletedLogs() {
        List<MaintenanceModel> logs = maintenanceRepository.findByStatus("COMPLETED");

        return logs.stream()
                .map(log -> {
                    VehicleModel vehicle = vehicleService.findById(log.getVehicleId());

                    return new MaintenanceLogViewModel(log, vehicle.getPlateNumber());
                })
                .collect(Collectors.toList());
    }

    /**
     * Finds all logs currently marked as IN_PROGRESS.
     */
    public long getActiveCount() {
        return maintenanceRepository.findByStatus("IN_PROGRESS").size();
    }

    @Transactional
    public void completeMaintenance(Long logId) {
        MaintenanceModel log = maintenanceRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Maintenance Log ID: " + logId));

        log.setStatus("COMPLETED");
        log.setDateBackActual(LocalDate.now());
        maintenanceRepository.save(log);

        if (log.getVehicleId() != null) {
            VehicleModel vehicle = vehicleService.findById(log.getVehicleId());
            vehicle.setStatus("AVAILABLE");
            vehicleService.saveVehicle(vehicle);
        }
    }

    public List<MaintenanceLogViewModel> findActiveLogs() {
        List<MaintenanceModel> logs = maintenanceRepository.findByStatus("IN_PROGRESS");

        return logs.stream()
                .map(log -> {
                    VehicleModel vehicle = vehicleService.findById(log.getVehicleId());

                    return new MaintenanceLogViewModel(log, vehicle.getPlateNumber());
                })
                .collect(Collectors.toList());
    }

}