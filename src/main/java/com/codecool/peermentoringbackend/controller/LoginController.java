package com.codecool.peermentoringbackend.controller;

import com.codecool.peermentoringbackend.entity.UserEntity;
import com.codecool.peermentoringbackend.model.GoogleLogin;
import com.codecool.peermentoringbackend.model.LoginCredential;
import com.codecool.peermentoringbackend.security.JwtTokenServices;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenServices jwtTokenServices;

    @Autowired
    private RegistrationController registrationController;

    public LoginController(AuthenticationManager authenticationManager, JwtTokenServices jwtTokenServices) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenServices = jwtTokenServices;
    }


    @GetMapping(value = "/authentication")
    public void authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String tokenFromRequest = jwtTokenServices.getTokenFromRequest(request);

        boolean authenticated = jwtTokenServices.validateToken(tokenFromRequest);

        if (authenticated) {
           response.setStatus(200);

        } else {
            response.setStatus(401);
            response.getWriter().println("user not authenticated");
        }

    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody LoginCredential loginCredential) {
        try {
            String username = loginCredential.getUsername();
            String password = loginCredential.getPassword();
            System.out.println("POST /auth/login: User { password: " + password + " }");
            return authenticationCookie(username, password);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @PostMapping(value = "/google", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity google(@RequestBody GoogleLogin google) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList("297452594902-lbd2etaj1i66sbnabbr3njqh6qqnigfi.apps.googleusercontent.com"))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(google.getTokenId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid Google token!");
        }

        if (idToken == null) {
            return ResponseEntity.badRequest().body("Invalid Google token!");
        }

        Payload payload = idToken.getPayload();
        UserEntity user = registrationController.doWithGoogle(payload);
        System.out.println("POST /auth/google: User { password: " + user.getPassword() + " }");
        return authenticationCookie(user.getUsername(), user.getPassword());
    }

    @GetMapping(value = "/logout")
    public ResponseEntity logout(){
        ResponseCookie cookie = ResponseCookie
                .from("authentication", "")
                .maxAge(0)
                .path("/").httpOnly(false).secure(false).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("");
    }

    private ResponseEntity authenticationCookie(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());


        String token = jwtTokenServices.createToken(username, roles);
        ResponseCookie cookie = ResponseCookie
                .from("authentication", token)
                .maxAge(3600)  //18 hrs
                .path("/").httpOnly(false).secure(false).build();



        Map<Object, Object> model = new HashMap<>();
        model.put("username", username);
        model.put("roles", roles);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(model);
    }
}
