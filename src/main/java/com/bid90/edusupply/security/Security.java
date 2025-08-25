package com.bid90.edusupply.security;

import com.bid90.edusupply.filters.JwtRequestFilter;
import com.bid90.edusupply.model.Role;
import com.bid90.edusupply.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Security configuration class for the application.
 * <p>
 * Configures JWT-based authentication, CORS, role-based access control, and Swagger integration.
 * </p>
 */
@EnableWebSecurity
@Component
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Staging server")})
public class Security {

    private final JwtRequestFilter jwtRequestFilter;
    private final UserService userService;
    private final List<String> allowedOrigins;
    private final Boolean allowCredentials;

    /**
     * Constructor for Security configuration.
     *
     * @param jwtRequestFilter  the JWT filter for validating tokens
     * @param userService       the UserService for loading user details
     * @param allowedOrigins    list of allowed CORS origins
     * @param allowCredentials  flag indicating whether CORS requests allow credentials
     */
    public Security(JwtRequestFilter jwtRequestFilter,
                    @Lazy UserService userService,
                    @Value("${cors.allowed-origins}") List<String> allowedOrigins,
                    @Value("${cors.allowed-credentials}") Boolean allowCredentials) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.userService = userService;
        this.allowedOrigins = allowedOrigins;
        this.allowCredentials = allowCredentials;
    }

    /**
     * Provides the PasswordEncoder bean for encoding passwords.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides the UserDetailsService bean to load user details for authentication.
     *
     * @return a UserDetailsService instance
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userService::loadUserByUsername;
    }

    /**
     * Configures the AuthenticationManager for the application.
     * <p>
     * Uses an internal DaoAuthenticationProvider with UserDetailsService and PasswordEncoder.
     * </p>
     *
     * @param userDetailsService the UserDetailsService bean
     * @param passwordEncoder    the PasswordEncoder bean
     * @return a configured AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    /**
     * Configures the SecurityFilterChain for HTTP requests.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/h2-console/**",
                                "/api/auth/**",
                                "/v3/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/v2/api-docs/**",
                                "/swagger-resources/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/group").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/group").hasAnyAuthority(Role.ADMIN.name(),Role.MANAGER.name())
                        .requestMatchers(HttpMethod.GET, "/api/group/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/group/**").hasAnyAuthority(Role.ADMIN.name(),Role.MANAGER.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/group/**").hasAnyAuthority(Role.ADMIN.name(),Role.MANAGER.name())

                        .requestMatchers(HttpMethod.GET, "/api/user").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/user").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/user/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/user/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/user/**").hasAuthority(Role.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures the CORS (Cross-Origin Resource Sharing) source.
     *
     * @return the configured CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "refresh-token"));
        configuration.setExposedHeaders(List.of("x-auth-token", "Content-Disposition"));
        configuration.setAllowCredentials(allowCredentials);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
