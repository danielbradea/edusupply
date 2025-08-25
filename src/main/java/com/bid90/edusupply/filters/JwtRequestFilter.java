package com.bid90.edusupply.filters;

import com.bid90.edusupply.model.User;
import com.bid90.edusupply.service.JwtTokenService;
import com.bid90.edusupply.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Optional;

/**
 * JWT Request Filter for handling authentication on each request.
 * Ensures that requests with a valid JWT token get authenticated.
 */
@Order(1)
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class.getName());
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    /**
     * Constructor for JwtRequestFilter.
     * @param jwtTokenService Service for handling JWT operations.
     * @param userService Service for retrieving user details.
     */
    public JwtRequestFilter(JwtTokenService jwtTokenService, @Lazy UserService userService) {
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
    }

    /**
     * Filters each request to check for a valid JWT token and set authentication context.
     *
     * @param request  HTTP servlet request
     * @param response HTTP servlet response
     * @param chain    Filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an input-output error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String jwt = extractJwtFromRequest(request);

        if (jwt != null) {
            processJwtAuthentication(jwt, request);
        }
        chain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the request header.
     *
     * @param request HTTP servlet request
     * @return Extracted JWT token or null if not present
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    /**
     * Processes JWT authentication by verifying the token and setting authentication context.
     *
     * @param jwt     The JWT token
     * @param request The HTTP request
     */
    private void processJwtAuthentication(String jwt, HttpServletRequest request) {
        try {
            var decodedJWT = jwtTokenService.verificationToken(jwt);
            String userId = decodedJWT.getSubject();
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticateUser(userId, request);
            }
        } catch (Exception e) {
            logger.warn("Invalid JWT Token: {}", e.getMessage());
        }
    }

    /**
     * Authenticates the user based on the JWT token's subject (UUID).
     *
     * @param userId Long of the user
     * @param request  HTTP servlet request
     */
    private void authenticateUser(String userId, HttpServletRequest request) {
        Optional<User> userOptional = userService.getUserById(Long.valueOf(userId));

        userOptional.ifPresentOrElse(user -> {

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }, () -> {
            logger.warn("JWT Token validation failed for user ID: {}", userId);
        });
    }
}