package com.routeiq.auth.controller;

import com.routeiq.auth.dto.JwtResponse;
import com.routeiq.auth.dto.LoginRequest;
import com.routeiq.auth.dto.SignupRequest;
import com.routeiq.common.response.MessageResponse;
import com.routeiq.security.JwtUtils;
import com.routeiq.security.UserDetailsImpl;
import com.routeiq.users.entity.Role;
import com.routeiq.users.entity.User;
import com.routeiq.users.entity.UserSettings;
import com.routeiq.users.repository.UserRepository;
import com.routeiq.users.repository.UserSettingsRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    UserSettingsRepository userSettingsRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, 
                userDetails.getId(), 
                userDetails.getUsername(), 
                userDetails.getDisplayName(), 
                userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setDisplayName(signUpRequest.getDisplayName());
        user.setPasswordHash(encoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.USER);
        
        userRepository.save(user);

        // Create default settings
        UserSettings settings = new UserSettings();
        settings.setUser(user);
        settings.setPreferredMode("FASTEST");
        settings.setDefaultTravelMode("DRIVING");
        settings.setCurrencyCode("USD");
        settings.setDistanceUnit("KM");
        settings.setTheme("LIGHT");
        settings.setFuelEfficiencyKmPerLitre(15.0);
        settings.setFuelPricePerLitre(1.50);
        userSettingsRepository.save(settings);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(new MessageResponse("Not authenticated"));
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(userDetails);
    }
}
