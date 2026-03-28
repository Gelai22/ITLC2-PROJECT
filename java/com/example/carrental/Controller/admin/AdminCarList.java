package com.example.carrental.Controller.admin;

import com.example.carrental.Models.VehicleModel;
import com.example.carrental.Services.VehicleService;
import com.example.carrental.Security.AdminPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/car")
public class AdminCarList {

    private final VehicleService vehicleService;

    private static final String EXTERNAL_UPLOAD_DIR = System.getProperty("user.home") + "/carrental_uploads/vehicles/";
    private static final String WEB_PATH = "/images/vehicles/";

    public AdminCarList(VehicleService vehicleService) {
        this.vehicleService = vehicleService;

        try {
            Files.createDirectories(Paths.get(EXTERNAL_UPLOAD_DIR));
        } catch (Exception ex) {
        }
    }

    @GetMapping("/list")
    public String carList(
            Model model,
            @AuthenticationPrincipal AdminPrincipal adminPrincipal,
            @RequestParam(defaultValue = "plateNumber") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {

        if (adminPrincipal != null) {
            model.addAttribute("adminUser", adminPrincipal.getAdmin());
        }

        List<VehicleModel> cars = vehicleService.findAllVehicles(sortField, sortDir);

        model.addAttribute("cars", cars);
        model.addAttribute("totalCarsCount", vehicleService.getVehicleCount());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "admin/car_list";
    }

    @GetMapping("/add")
    public String addCar(Model model, @AuthenticationPrincipal AdminPrincipal adminPrincipal) {
        if (adminPrincipal != null) {
            model.addAttribute("adminUser", adminPrincipal.getAdmin());
        }

        model.addAttribute("vehicle", new VehicleModel());
        model.addAttribute("pageTitle", "Add New Vehicle");
        return "admin/add_car";
    }

    @GetMapping("/edit/{id}")
    public String editCar(
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal AdminPrincipal adminPrincipal) {

        if (adminPrincipal != null) {
            model.addAttribute("adminUser", adminPrincipal.getAdmin());
        }

        VehicleModel vehicle = vehicleService.findById(id);

        model.addAttribute("vehicle", vehicle);
        model.addAttribute("pageTitle", "Edit Vehicle: " + vehicle.getPlateNumber());

        return "admin/add_car";
    }

    @PostMapping("/save")
    public String saveCar(@ModelAttribute("vehicle") VehicleModel vehicle,
                          @RequestParam("vehicleImage") MultipartFile file,
                          RedirectAttributes redirectAttributes) {

        if (!file.isEmpty()) {
            try {
                Path uploadPath = Paths.get(EXTERNAL_UPLOAD_DIR);

                String originalFileName = file.getOriginalFilename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String newFileName = UUID.randomUUID().toString() + fileExtension;
                Path targetLocation = uploadPath.resolve(newFileName);

                Files.copy(file.getInputStream(), targetLocation);

                vehicle.setImagePath(WEB_PATH + newFileName);

            } catch (IOException ex) {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload file: " + ex.getMessage());
                return "redirect:/car/add";
            }
        } else if (vehicle.getId() != null && (vehicle.getImagePath() == null || vehicle.getImagePath().isEmpty())) {
            vehicleService.getVehicleById(vehicle.getId()).ifPresent(existingVehicle -> {
                vehicle.setImagePath(existingVehicle.getImagePath());
            });
        }

        vehicleService.saveVehicle(vehicle);

        String message = (vehicle.getId() == null) ? "Vehicle added successfully!" : "Vehicle updated successfully!";
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/car/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vehicleService.archiveVehicle(id);
            redirectAttributes.addFlashAttribute("successMessage", "Vehicle archived successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/car/list";
    }
}