package com.talentbridge.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        // Public authentication APIs
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login"
                        )
                        .permitAll()


                        // Public vacancy listing
                        .requestMatchers(
                                "/api/v1/public/vacancies",
                                "/api/v1/public/vacancies/**"
                        )
                        .permitAll()


                        // Admin APIs
                        .requestMatchers(
                                "/api/admin/**"
                        )
                        .hasRole("ADMIN")


                        // Candidate can view interview schedule
                        // HR/Admin can also view interviews
                        .requestMatchers(
                                "/api/interviews/application/**"
                        )
                        .hasAnyRole(
                                "CANDIDATE",
                                "HR",
                                "ADMIN"
                        )


                        // HR/Admin interview management
                        // Schedule interview
                        // Update interview status
                        .requestMatchers(
                                "/api/interviews/**"
                        )
                        .hasAnyRole(
                                "HR",
                                "ADMIN"
                        )


                        // HR/Admin vacancy management
                        .requestMatchers(
                                "/api/v1/hr/**",
                                "/api/hr/**",
                                "/api/vacancies/**"
                        )
                        .hasAnyRole(
                                "HR",
                                "ADMIN"
                        )


                        // Candidate APIs
                        .requestMatchers(
                                "/api/v1/candidate/**",
                                "/api/candidate/**"
                        )
                        .hasAnyRole(
                                "CANDIDATE",
                                "ADMIN"
                        )


                        // User profile
                        .requestMatchers(
                                "/api/users/**"
                        )
                        .authenticated()


                        // Everything else requires login
                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}