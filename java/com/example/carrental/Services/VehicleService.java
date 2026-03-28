package com.example.carrental.Services;

import com.example.carrental.Models.VehicleModel;
import com.example.carrental.Repositories.VehicleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public VehicleModel saveVehicle(VehicleModel vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public List<VehicleModel> findAllVehicles(String sortField, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField);
        return vehicleRepository.findByStatusNot("ARCHIVED", sort);
    }

    public List<VehicleModel> findFilteredVehicles(
            String search, String vehicleType, String priceRange, String sortField, String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField);

        String finalSearch = (search != null && !search.isEmpty()) ? search : null;
        String finalVehicleType = (vehicleType != null && !vehicleType.isEmpty()) ? vehicleType : null;

        List<VehicleModel> filteredVehicles = vehicleRepository.findByFilterCriteria(finalSearch, finalVehicleType, sort);

        return filteredVehicles.stream()
                .filter(v -> {
                    if (priceRange == null || priceRange.isEmpty()) return true;

                    BigDecimal price = v.getRentalPrice();

                    return switch (priceRange) {
                        case "under500" -> price.compareTo(new BigDecimal("500.00")) < 0;
                        case "500to1000" -> price.compareTo(new BigDecimal("500.00")) >= 0 && price.compareTo(new BigDecimal("1000.00")) <= 0;
                        case "over1000" -> price.compareTo(new BigDecimal("1000.00")) > 0;
                        default -> true;
                    };
                })
                .collect(Collectors.toList());
    }

    public VehicleModel findById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle ID: " + id));
    }

    public Optional<VehicleModel> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        vehicleRepository.deleteById(id);
    }

    @Transactional
    public void archiveVehicle(Long id) {
        VehicleModel vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Vehicle not found with ID: " + id));

        vehicle.setStatus("ARCHIVED");
        vehicleRepository.save(vehicle);
    }

    public long getVehicleCount() {
        return vehicleRepository.count();
    }

    public List<VehicleModel> findAvailableForMaintenance() {
        return vehicleRepository.findAll().stream()
                .filter(v -> !"MAINTENANCE".equals(v.getStatus()))
                .collect(Collectors.toList());
    }

    @Transactional
    public VehicleModel updateVehicle(VehicleModel vehicle) {
        return vehicleRepository.save(vehicle);
    }
}