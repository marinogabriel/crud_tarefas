package elabbora.backend.controller;

import elabbora.backend.dto.LoginDTO;
import elabbora.backend.dto.RegisterDTO;
import elabbora.backend.model.User;
import elabbora.backend.repository.UserRepository;
import elabbora.backend.service.AuthService;
import elabbora.backend.utils.Jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserRepository userRepository;

    private final Jwt jwt;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterDTO credentialsDTO) {

        log.info("LOG - register request: {}", credentialsDTO.getEmail());

        User res = this.authService.register(credentialsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO credentialsDTO) {
        Map<String, Object> loginResponse = authService.login(credentialsDTO);

        return ResponseEntity.ok(Map.of(
                "token", loginResponse.get("token"),
                "refreshToken", loginResponse.get("refreshToken")
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> payload) {
        String refreshToken = payload.get("refreshToken");

        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("Refresh token is required.");
        }

        try {
            Claims claims = jwt.decodeAndVerifyToken(refreshToken);

            UUID userId = UUID.fromString(claims.getSubject());
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String newAccessToken = jwt.generateJwtToken(user);
            String newRefreshToken = jwt.generateRefreshToken(user);

            return ResponseEntity.ok(Map.of(
                    "token", newAccessToken,
                    "refreshToken", newRefreshToken
            ));

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired.");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token.");
        }
    }

}
