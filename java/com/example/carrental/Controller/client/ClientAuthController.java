package com.example.carrental.Controller.client;

import com.example.carrental.Models.client.ClientModel;
import com.example.carrental.Services.client.ClientAuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;

@Controller
public class ClientAuthController {

    private final ClientAuthService clientAuthService;

    public ClientAuthController(ClientAuthService clientAuthService) {
        this.clientAuthService = clientAuthService;
    }

    @PostMapping("/register")
    public String processClientRegistration(
            @Valid @ModelAttribute("client") ClientModel client,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/register_client";
        }

        try {
            clientAuthService.registerNewClient(client);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("emailExistsError", "The email address is already registered.");
            return "redirect:/register";
        }

        redirectAttributes.addFlashAttribute("registered", true);
        return "redirect:/login";
    }
}