package com.base.authproject.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
            .csrf()
            .disable()
            .authorizeHttpRequests()
                //APIs that can be accessed by the admin.
                .requestMatchers("/path_api"

                                    
                                ).hasAnyRole("ADMIN")
                
                // APIs that can be accessed by both the admin and the user.
                .requestMatchers("/path_api"
                
                                ).hasAnyRole("ADMIN", "USER")

                // Apis that can accessed by the user
                .requestMatchers("/path_api"
                                    
                                ).hasAnyRole("USER")

                // Apis that can accessed by anyone
                .requestMatchers("/auth/**").permitAll()

            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

}
