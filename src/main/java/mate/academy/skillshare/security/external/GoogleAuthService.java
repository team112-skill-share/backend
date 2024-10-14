package mate.academy.skillshare.security.external;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.user.GoogleUserDto;
import mate.academy.skillshare.dto.user.UserLoginResponseDto;
import mate.academy.skillshare.exception.AuthenticationException;
import mate.academy.skillshare.model.User;
import mate.academy.skillshare.repository.user.UserRepository;
import mate.academy.skillshare.security.internal.JwtUtil;
import mate.academy.skillshare.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";
    private static final String REDIRECT_URI = "http://localhost:8080/api/auth/google/callback";
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private static String CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private static String CLIENT_SECRET;
    private final JwtUtil jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RestTemplate restTemplate;

    public void initiateGoogleLogin(HttpServletResponse response) throws IOException {
        String googleAuthUrl = new StringBuilder("https://accounts.google.com/o/oauth2/v2/auth")
                .append("?client_id=")
                .append(URLEncoder.encode(CLIENT_ID, StandardCharsets.UTF_8))
                .append("&redirect_uri=")
                .append(URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8))
                .append("&response_type=code")
                .append("&scope=openid%20email%20profile")
                .toString();
        response.sendRedirect(googleAuthUrl);
    }

    public ResponseEntity<?> handleGoogleLoginCallback(String authorizationCode) {
        try {
            String accessToken = exchangeAuthorizationCodeForAccessToken(authorizationCode);
            GoogleUserDto googleUser = fetchGoogleUserInfo(accessToken);
            Optional<User> existingUser = userRepository.findByEmail(googleUser.email());
            User user;
            user = existingUser.orElseGet(() -> userService.registerGoogleUser(googleUser));
            String jwtToken = jwtTokenProvider.generateToken(user.getUsername());
            return ResponseEntity.ok(new UserLoginResponseDto(jwtToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Google authentication failed");
        }
    }

    private String exchangeAuthorizationCodeForAccessToken(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("grant_type", "authorization_code");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                TOKEN_URL, HttpMethod.POST, request, Map.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        }
        throw new AuthenticationException("Failed to exchange authorization code for access token");
    }

    private GoogleUserDto fetchGoogleUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                USER_INFO_URL, HttpMethod.GET, request, Map.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> userInfo = response.getBody();
            return new GoogleUserDto(
                    (String) userInfo.get("email"),
                    (String) userInfo.get("name")
            );
        }
        throw new AuthenticationException("Failed to fetch Google user info");
    }
}
