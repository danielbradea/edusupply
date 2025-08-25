package com.bid90.edusupply.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bid90.edusupply.PasswordUtil;
import com.bid90.edusupply.dto.auth.JwtTokens;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JwtTokenService is responsible for generating, validating, and extracting information from JWT tokens.
 * It handles both access and refresh tokens, and provides methods for extracting claims and checking expiration.
 */
@Component
public class JwtTokenService {

    private final String secret;
    private final String secretFilePath;
    private final Long jwtExpirationMs;
    private final Long refreshJwtExpirationMs;

    /**
     * Constructs a JwtTokenService with the specified parameters.
     *
     * @param secret                 The secret key used for signing JWT tokens.
     * @param secretFilePath         The file path where the secret key is stored (if applicable).
     * @param jwtExpirationMs        The expiration time for the access token in milliseconds.
     * @param refreshJwtExpirationMs The expiration time for the refresh token in milliseconds.
     */
    public JwtTokenService(@Value("${jwt.secret}") String secret,  @Value("${jwt.secret.file}") String secretFilePath,
                           @Value("${jwt.expiration}") Long jwtExpirationMs,  @Value("${jwt.refresh.expiration}") Long refreshJwtExpirationMs) {
        this.secret = secret;
        this.secretFilePath = secretFilePath;
        this.jwtExpirationMs = jwtExpirationMs;
        this.refreshJwtExpirationMs = refreshJwtExpirationMs;
    }

    /**
     * Extracts the subject from the provided JWT token.
     *
     * @param token The JWT token.
     * @return The subject of the token.
     */
    public String getSubject(String token) {
        return JWT.decode(token).getSubject();
    }

    /**
     * Extracts a specific claim from the provided JWT token.
     *
     * @param token  The JWT token.
     * @param claim  The claim to extract.
     * @param tClass The type of the claim.
     * @param <T>    The type of the claim.
     * @return The value of the specified claim.
     * @throws IllegalArgumentException if the claim is invalid or cannot be extracted.
     */
    public <T> T getClaim(String token, String claim, Class<T> tClass) {
        try {
            return JWT.decode(token).getClaim(claim).as(tClass);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token claim", e);
        }
    }

    /**
     * Checks if the provided JWT token has expired.
     *
     * @param token The JWT token.
     * @return True if the token is expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token)
                .before(new Date());
    }

    /**
     * Extracts the expiration date from the provided JWT token.
     *
     * @param token The JWT token.
     * @return The expiration date of the token.
     */
    private Date extractExpiration(String token) {
        return getClaim(token, "expirationAt", Date.class);
    }

    /**
     * Validates the provided JWT token using the configured secret.
     *
     * @param token The JWT token to validate.
     * @return The decoded JWT token if valid.
     */
    public DecodedJWT verificationToken(String token) {
        var validation = JWT.require(Algorithm.HMAC512(PasswordUtil.
                getPassword(secret, secretFilePath))).build();
        return validation.verify(token);
    }

    /**
     * Generates a new JWT token with the specified subject and claims.
     *
     * @param subject The subject to associate with the token.
     * @param claims  A map of dynamic claims to include in the token.
     * @return The generated JWT token.
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        // Create the JWT builder
        var jwtBuilder = JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date((new Date()).getTime() + jwtExpirationMs));

        // Add dynamic claims
        if (claims != null) {
            claims.forEach((key, value) -> {
                // Ensure the claim is added with the appropriate value type
                switch (value) {
                    case String s -> jwtBuilder.withClaim(key, s);
                    case Boolean b -> jwtBuilder.withClaim(key, b);
                    case Long l -> jwtBuilder.withClaim(key, l);
                    case Integer i -> jwtBuilder.withClaim(key, i);
                    case Double d -> jwtBuilder.withClaim(key, d);
                    case null, default -> {
                        if (value != null) {
                            jwtBuilder.withClaim(key, value.toString());
                        }
                    }
                }
            });
        }
        // Return the generated token
        return jwtBuilder.sign(Algorithm.HMAC512(PasswordUtil.getPassword(secret, secretFilePath)));
    }

    /**
     * Generates both an access token and a refresh token for the specified subject, with different claims for each token.
     *
     * @param subject            The subject to associate with the tokens.
     * @param claimsAccessToke   A map of claims for the access token.
     * @param claimsRefreshToken A map of claims for the refresh token.
     * @return A JwtTokens object containing both the access token and the refresh token.
     */
    public JwtTokens generateTokens(String subject,
                                    Map<String, Object> claimsAccessToke,
                                    Map<String, Object> claimsRefreshToken) {
        return new JwtTokens(
                generateToken(subject, claimsAccessToke),
                jwtExpirationMs,
                generateToken(subject, claimsRefreshToken),
                refreshJwtExpirationMs);
    }

}