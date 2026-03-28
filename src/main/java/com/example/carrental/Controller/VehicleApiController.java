package com.example.carrental.Controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*") 
public class VehicleApiController {

    @GetMapping("/all")
    public List<Map<String, Object>> getAllVehicles() {
        List<Map<String, Object>> vehicles = new ArrayList<>();
        
        Map<String, Object> car1 = new HashMap<>();
        car1.put("id", 1);
        car1.put("brand", "Toyota");
        car1.put("model", "Vios");
        car1.put("price", 1500);
        
        vehicles.add(car1);
        return vehicles;
    }
}
