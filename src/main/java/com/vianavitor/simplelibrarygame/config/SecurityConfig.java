package com.vianavitor.simplelibrarygame.config;

import com.vianavitor.simplelibrarygame.config.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers("/api/students/register").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/administrators/register").permitAll()
                        .requestMatchers("/api/classrooms/**", "/api/administrators/**").hasRole("ADM")
                        .requestMatchers("/api/students/**").hasAnyRole("ADM", "STUDENT")
                        .requestMatchers("/api/groups/**", "/api/group-books/**").hasRole("STUDENT")
                        .requestMatchers("/api/summaries/**").hasRole("STUDENT")
                        .requestMatchers("/api/stats/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/stats/**").hasRole("PROFESSOR")
                        .requestMatchers(HttpMethod.GET, "/api/summaries/**").hasRole("PROFESSOR")
                        .requestMatchers("/api/professors/**").hasAnyRole("ADM", "PROFESSOR")
                        .requestMatchers("/api/librarians/**").hasAnyRole("ADM", "LIBRARIAN")
                        .requestMatchers(HttpMethod.POST, "/api/books/**").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("LIBRARIAN")
                        .requestMatchers("/api/test/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

