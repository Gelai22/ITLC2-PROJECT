package com.example.carrental.Controller.client;

import com.example.carrental.Models.VehicleModel;
import com.example.carrental.Services.VehicleService;
import com.example.carrental.Security.ClientPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ClientBrowse {

    private final VehicleService vehicleService;

    public ClientBrowse(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping({ "/browse" })
    public String browseCars(
            Model model,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String vehicleType,
            @RequestParam(required = false) String priceRange,
            @RequestParam(defaultValue = "rentalPrice") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        if (clientPrincipal != null) {
            model.addAttribute("clientUser", clientPrincipal.getClient());
        }

        List<VehicleModel> vehicles = vehicleService.findFilteredVehicles(
                search, vehicleType, priceRange, sortField, sortDir);

        model.addAttribute("vehicles", vehicles);

        model.addAttribute("search", search);
        model.addAttribute("vehicleType", vehicleType);
        model.addAttribute("priceRange", priceRange);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "client/browse";
    }
}