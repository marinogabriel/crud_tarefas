package elabbora.backend.service;

import elabbora.backend.dto.LoginDTO;
import elabbora.backend.dto.RegisterDTO;
import elabbora.backend.exception.AlreadyExistsException;
import elabbora.backend.exception.NotFoundException;
import elabbora.backend.model.User;
import elabbora.backend.repository.UserRepository;
import elabbora.backend.utils.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Jwt jwt;

    public User register(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new AlreadyExistsException("User");
        }

        User tempUser = new User();
        tempUser.setId(UUID.randomUUID());
        tempUser.setEmail(registerDTO.getEmail());

        User newUser = new User();
        newUser.setFirstName(registerDTO.getFirstName());
        newUser.setLastName(registerDTO.getLastName());
        newUser.setEmail(registerDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        return userRepository.save(newUser);
    }

    public Map<String, Object> login(LoginDTO credentialsDTO) {
        User user = userRepository.findByEmail(credentialsDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("User"));

        if (!passwordEncoder.matches(credentialsDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwt.generateJwtToken(user);
        String refreshToken = jwt.generateRefreshToken(user);

        return Map.of(
                "token", token,
                "refreshToken", refreshToken
        );
    }

}
