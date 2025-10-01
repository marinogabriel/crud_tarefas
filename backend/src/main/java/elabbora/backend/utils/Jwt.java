package elabbora.backend.utils;

import elabbora.backend.exception.InvalidTokenException;
import elabbora.backend.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class Jwt {

  @Value("${JWT_SECRET}")
  private String JwtKey;

  public String generateJwtToken(User user) {
    var jwtBuilder = Jwts.builder()
        .setSubject(user.getId().toString())
        .claim("email", user.getEmail())
        .claim("firstName", user.getFirstName())
        .claim("lastName", user.getLastName())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 10 minutes expiration
        .signWith(Keys.hmacShaKeyFor(this.JwtKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256);

    return jwtBuilder.compact();
  }

  public String generateRefreshToken(User user) {
    return Jwts.builder()
        .setSubject(user.getId().toString())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12)) // 12 hours expiration
        .signWith(Keys.hmacShaKeyFor(this.JwtKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateServiceJwtToken() {
    return Jwts.builder()
        .setSubject("service-account")
        .claim("role", "SERVICE")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) // 5 minutes expiration
        .signWith(Keys.hmacShaKeyFor(this.JwtKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
        .compact();
  }

  public Claims decodeJwtToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(this.JwtKey.getBytes(StandardCharsets.UTF_8))
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public Boolean verifyToken(String token) {
    try {
      Claims claims = decodeJwtToken(token);
      return !claims.getExpiration().before(new Date());
    } catch (JwtException e) {
      return false;
    }
  }

  public Claims decodeAndVerifyToken(String token) throws InvalidTokenException {
    try {
      return decodeJwtToken(token);
    } catch (ExpiredJwtException e) {
      throw e;
    } catch (JwtException e) {
      throw new InvalidTokenException();
    }
  }
}
