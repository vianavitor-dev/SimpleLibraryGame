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
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
//                        ENDPOINTS PERMISSIONS:
//                        * Students permissions
                                .requestMatchers(HttpMethod.POST, "/api/students/register").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/students/**").hasAnyRole("ADM", "STUDENT", "PROFESSOR")
                                .requestMatchers("/api/students/**").hasAnyRole("ADM", "STUDENT")

//                        * Administrators permissions
//                                .requestMatchers(HttpMethod.POST, "/api/administrators/register").permitAll()
                                .requestMatchers("/api/administrators/**").hasRole("ADM")

//                        * Professors permissions
                                .requestMatchers("/api/professors/**").hasAnyRole("ADM", "PROFESSOR")

//                        * Librarians permissions
                                .requestMatchers("/api/librarians/**").hasAnyRole("ADM", "LIBRARIAN")

//                        * Classrooms permissions
                                .requestMatchers(HttpMethod.GET, "/api/classrooms/**").hasAnyRole("ADM", "PROFESSOR", "STUDENT")
                                .requestMatchers("/api/classrooms/**").hasRole("ADM")

//                        * Books permissions
                                .requestMatchers(HttpMethod.POST, "/api/books/**").hasRole("LIBRARIAN")
                                .requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("LIBRARIAN")

//                        * Stats permissions
                                .requestMatchers(HttpMethod.GET, "/api/stats/**").hasAnyRole("ADM", "PROFESSOR", "STUDENT")
                                .requestMatchers("/api/stats/**").hasRole("STUDENT")

//                        * Summaries permissions
                                .requestMatchers(HttpMethod.GET, "/api/summaries/**").hasAnyRole("PROFESSOR", "STUDENT")
                                .requestMatchers("/api/summaries/**").hasRole("STUDENT")

//                        * History permissions
                                .requestMatchers(HttpMethod.GET, "/api/read-history").hasAnyRole("PROFESSOR", "STUDENT")
                                .requestMatchers(HttpMethod.POST, "/api/read-history").hasRole("STUDENT")

//                        * Groups/Group-Books permissions
                                .requestMatchers("/api/groups/**", "/api/group-books/**").hasRole("STUDENT")
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

