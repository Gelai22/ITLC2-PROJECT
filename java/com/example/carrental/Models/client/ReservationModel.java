package com.example.carrental.Models.client;

import com.example.carrental.Models.VehicleModel;
import com.example.carrental.Models.admin.AdminModel;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity(name = "reservation")
public class ReservationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Reservation_Id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Customer_Id", nullable = false, insertable = true, updatable = true)
    private ClientModel client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Vehicle_Id", nullable = false)
    private VehicleModel vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Employee_Id", nullable = true)
    private AdminModel employee;

    @Column(name = "reservation_date")
    private LocalDateTime reservationDate;

    @Column(name = "pickup_date")
    private LocalDateTime pickupDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "pickup_location")
    private String pickupLocation;

    @Column(name = "Client_Feedback") // Renamed DB column: Client_Feedback
    private String clientFeedback; // Renamed Java field

    @Column(name = "Payment_Date")
    private LocalDate paymentDate;

    @Column(name = "Payment_Method")
    private String paymentMethod;

    @Column(name = "Payment_Status")
    private String paymentStatus;

    @Column(name = "Rating") // Renamed DB column: Rating
    private String rating; // Renamed Java field

    @Column(name = "Total_Cost", precision = 10, scale = 2)
    private BigDecimal totalCost;

    private String status;

    public ReservationModel() {
        this.reservationDate = LocalDateTime.now();
        this.status = "PENDING";
        this.paymentStatus = "UNPAID";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ClientModel getClient() { return client; }
    public void setClient(ClientModel client) { this.client = client; }

    public VehicleModel getVehicle() { return vehicle; }
    public void setVehicle(VehicleModel vehicle) { this.vehicle = vehicle; }

    public AdminModel getEmployee() { return employee; }
    public void setEmployee(AdminModel employee) { this.employee = employee; }

    public LocalDateTime getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDateTime reservationDate) { this.reservationDate = reservationDate; }

    public LocalDateTime getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDateTime pickupDate) { this.pickupDate = pickupDate; }

    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getClientFeedback() { return clientFeedback; }
    public void setClientFeedback(String clientFeedback) { this.clientFeedback = clientFeedback; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}