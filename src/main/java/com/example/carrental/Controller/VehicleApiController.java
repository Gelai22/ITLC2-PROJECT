package com.example.carrental.Controller;

import com.example.carrental.Models.VehicleModel;
import com.example.carrental.Repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*") 
public class VehicleApiController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @GetMapping("/all")
    public List<VehicleModel> getAllVehicles() {
        return vehicleRepository.findAll();
    }
}
