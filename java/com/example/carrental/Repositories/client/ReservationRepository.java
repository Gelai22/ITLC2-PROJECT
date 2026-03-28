package com.example.carrental.Repositories.client;

import com.example.carrental.Models.client.ReservationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationModel, Long> {

    @Query("SELECT r FROM reservation r JOIN FETCH r.client c JOIN FETCH r.vehicle v WHERE r.client.id = :clientId AND r.status IN :statuses")
    List<ReservationModel> findByClientIdAndStatusIn(@Param("clientId") Long clientId, @Param("statuses") List<String> statuses);

    @Query("SELECT r FROM reservation r JOIN FETCH r.client c JOIN FETCH r.vehicle v WHERE r.client.id = :clientId AND r.status = :status")
    List<ReservationModel> findByClientIdAndStatus(@Param("clientId") Long clientId, @Param("status") String status);

    @Query("SELECT r FROM reservation r JOIN FETCH r.client c JOIN FETCH r.vehicle v WHERE r.status = :status")
    List<ReservationModel> findByStatusWithDetails(@Param("status") String status);

    @Query("SELECT COUNT(DISTINCT r.client.id) FROM reservation r WHERE r.status = :status")
    Long countUniqueClientsByStatus(@Param("status") String status);
}