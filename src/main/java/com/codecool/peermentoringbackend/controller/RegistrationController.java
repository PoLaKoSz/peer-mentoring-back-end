package com.codecool.peermentoringbackend.controller;

import com.codecool.peermentoringbackend.entity.UserEntity;
import com.codecool.peermentoringbackend.model.RegResponse;
import com.codecool.peermentoringbackend.model.UserModel;
import com.codecool.peermentoringbackend.service.RegistrationService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/reg")
public class RegistrationController {

    @Autowired
    protected RegistrationService registrationService;

    @PostMapping(value = "/registration")
    public void doRegistration(HttpServletResponse response, @RequestBody UserModel userModel) throws IOException {

        RegResponse regResponse = registrationService.handleRegistration(userModel);
        if (regResponse.isSuccess()) {
            response.setStatus(200);
        } else {
            response.setStatus(400);
        }
        response.getWriter().println(regResponse.getMessage());
    }

    protected UserEntity doWithGoogle(GoogleIdToken.Payload payload) {
        return registrationService.withGoogle(payload);
    }
}
