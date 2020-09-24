package com.codecool.peermentoringbackend.controller;

import com.codecool.peermentoringbackend.model.UserModel;
import com.codecool.peermentoringbackend.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reg")
@CrossOrigin(origins = "http://localhost:3000")
public class RegistrationController {

    @Autowired
    protected RegistrationService registrationService;

    @PostMapping(value = "/registration")
    public String doRegistration(@RequestBody UserModel userModel) {
        return registrationService.handleRegistration(userModel);
    }
}
