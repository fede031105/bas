package com.upiiz.bas.controllers;

import com.upiiz.bas.models.User;
import com.upiiz.bas.repository.UserRepository;
import com.upiiz.bas.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // Endpoint para registrar un nuevo usuario cifrando su contraseña
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(request);
        String jwtToken = jwtService.generateToken(request);
        return ResponseEntity.ok("Usuario registrado exitosamente. Token JWT: " + jwtToken);
    }

    // Endpoint para iniciar sesión y generar el token JWT
    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(jwtToken);
    }
}