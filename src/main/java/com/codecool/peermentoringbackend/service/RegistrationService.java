package com.codecool.peermentoringbackend.service;

import com.codecool.peermentoringbackend.entity.UserEntity;
import com.codecool.peermentoringbackend.model.GoogleLogin;
import com.codecool.peermentoringbackend.model.RegResponse;
import com.codecool.peermentoringbackend.model.UserModel;
import com.codecool.peermentoringbackend.repository.UserRepository;
import com.codecool.peermentoringbackend.service.validation.ValidatorService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidatorService validatorService;

    private final PasswordEncoder passwordEncoder;

    RegistrationService() {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public RegResponse handleRegistration(UserModel userModel) {

        EmailValidator validator = EmailValidator.getInstance();


        if (!validator.isValid(userModel.getEmail())) return new RegResponse(false, "e-mail format not valid");
        if (userRepository.existsByEmail(userModel.getEmail()))
            return new RegResponse(false, "this email is already registered");
        if (userRepository.existsByUsername(userModel.getUsername()))
            return new RegResponse(false, "this username is already taken");
        if (!validatorService.validateRegistration(userModel, 2, 20,  2, 20))
            return new RegResponse(false, "registration failed due to invalid credentials");

        UserEntity userEntity = UserEntity.builder()
                .email(userModel.getEmail())
                .password(passwordEncoder.encode(userModel.getPassword()))
                .username(userModel.getUsername())
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .registrationDate(LocalDateTime.now())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        userRepository.save(userEntity);
        return new RegResponse(true, "success");
    }

    private final String googlePassword = "Admin1234$";

    public UserEntity withGoogle(GoogleIdToken.Payload payload) {
        String email = payload.getEmail();
        if (userRepository.existsByEmail(email)) {
            UserEntity user = userRepository.findDistinctByEmail(email);
            user.setPassword(passwordEncoder.encode(googlePassword));
            userRepository.save(user);
            user = userRepository.findDistinctByEmail(email);
            System.out.println("User (after password reset with Google Auth) { username: " + user.getUsername() + ", email: " + user.getEmail() + ", password: " + user.getPassword() + " }");
            user.setPassword(googlePassword);
            System.out.println("User (this will be authenticated) { username: " + user.getUsername() + ", email: " + user.getEmail() + ", password: " + user.getPassword() + " }");
            return user;
        }

        UserEntity userEntity = UserEntity.builder()
                .email(payload.getEmail())
                .password(passwordEncoder.encode(googlePassword))
                .username(payload.getEmail())
                .firstName((String) payload.get("given_name"))
                .lastName((String) payload.get("family_name"))
                .registrationDate(LocalDateTime.now())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        userRepository.save(userEntity);
        System.out.println("User with email address (" + email + ") didn't exist so created one");
        return userEntity;
    }
}
