package mate.academy.skillshare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.user.UserLoginRequestDto;
import mate.academy.skillshare.dto.user.UserLoginResponseDto;
import mate.academy.skillshare.dto.user.UserRegistrationRequestDto;
import mate.academy.skillshare.dto.user.UserResponseDto;
import mate.academy.skillshare.exception.RegistrationException;
import mate.academy.skillshare.security.external.GoogleAuthService;
import mate.academy.skillshare.security.internal.AuthenticationService;
import mate.academy.skillshare.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication managing", description = "Endpoints for managing authentication")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final GoogleAuthService googleAuthService;

    @Operation(summary = "Register new user", description = "Register new user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @Operation(summary = "Log in", description = "Log into existing account")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @Operation(summary = "Google login", description = "Redirect to Google OAuth for login")
    @GetMapping("/login/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        googleAuthService.initiateGoogleLogin(response);
    }

    @Operation(summary = "Google OAuth callback", description = "Handle Google OAuth callback")
    @GetMapping("/google/callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam("code") String authorizationCode) {
        return googleAuthService.handleGoogleLoginCallback(authorizationCode);
    }
}
