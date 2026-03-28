package com.example.carrental.Repositories;

import com.example.carrental.Models.VehicleModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleModel, Long> {

    VehicleModel findByPlateNumber(String plateNumber);

    long countByStatus(String status);

    List<VehicleModel> findByStatus(String status);

    List<VehicleModel> findByStatusNot(String status, Sort sort);

    @Query("SELECT v FROM vehicle v " +
            "WHERE v.status = 'AVAILABLE' " +
            "AND (:search IS NULL OR lower(v.vehicleModel) LIKE lower(concat('%', :search, '%')))" +
            "AND (:vehicleType IS NULL OR v.vehicleType = :vehicleType)")
    List<VehicleModel> findByFilterCriteria(@Param("search") String search, @Param("vehicleType") String vehicleType, Sort sort);
}