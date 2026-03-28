package com.example.carrental.Repositories.admin;

import com.example.carrental.Models.admin.MaintenanceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenanceModel, Long> {
    List<MaintenanceModel> findByStatus(String status);
}