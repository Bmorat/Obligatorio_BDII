package com.obligatorio.bdii.service;

import com.obligatorio.bdii.dto.AuthenticatedUser;
import com.obligatorio.bdii.dto.LoginRequest;
import com.obligatorio.bdii.dto.LoginResponse;
import com.obligatorio.bdii.repository.AuthRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthRepository authRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        AuthenticatedUser user = authRepository.findByCorreo(request.correo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas"));

        if (!passwordEncoder.matches(request.password(), user.passwordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(
                token,
                user.correo(),
                user.paisDoc(),
                user.tipoDoc(),
                user.numeroDoc(),
                user.rol());
    }
}
