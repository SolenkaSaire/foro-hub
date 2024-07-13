package org.forohub.auth.service;

import org.forohub.auth.response.AuthResponse;
import org.forohub.jwt.JwtAuthService;
import org.forohub.model.User;
import org.forohub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthService jwtAuthService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public AuthResponse register(User user) {
        user.setContrasena(passwordEncoder.encode(user.getContrasena()));
        userRepository.save(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getCorreoElectronico());
        String token = jwtAuthService.generateToken(userDetails);
        return new AuthResponse(token, "User registered successfully");
    }

    public AuthResponse login(User user) {
        User existingUser = userRepository.findByCorreoElectronico(user.getCorreoElectronico());
        if (existingUser != null && passwordEncoder.matches(user.getContrasena(), existingUser.getContrasena())) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getCorreoElectronico());
            String token = jwtAuthService.generateToken(userDetails);
            return new AuthResponse(token, "Login successful");
        }
        return new AuthResponse(null, "Invalid credentials");
    }
}
