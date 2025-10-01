package elabbora.backend.config;

import elabbora.backend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

  @Value("${JWT_SECRET}")
  private String JwtKey;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {

    String path = request.getRequestURI();

    List<String> skipPaths = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/refresh"
    );

    if (skipPaths.stream().anyMatch(path::startsWith)) {
      filterChain.doFilter(request, response);
      return;
    }

    String header = request.getHeader("Authorization");

    if (header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = header.substring(7);

    try {
      JwtParser parser = Jwts.parserBuilder()
              .setSigningKey(Keys.hmacShaKeyFor(JwtKey.getBytes()))
              .build();

      Claims claims = parser.parseClaimsJws(token).getBody();

      String email = claims.get("email", String.class);
      String name = claims.get("name", String.class);
      UUID userId = UUID.fromString(claims.getSubject());

      User user = new User(userId, email, name);

      Authentication auth = new UsernamePasswordAuthenticationToken(user, null, null);
      SecurityContextHolder.getContext().setAuthentication(auth);

    } catch (ExpiredJwtException e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Token expired");
      return;
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Invalid token");
      return;
    }

    filterChain.doFilter(request, response);
  }

}