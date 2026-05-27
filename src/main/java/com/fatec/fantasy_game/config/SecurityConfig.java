package com.fatec.fantasy_game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita CSRF pois usaremos tokens/APIs REST mais para frente
                .csrf(csrf -> csrf.disable())

                // Define quais caminhos estão liberados ou protegidos
                .authorizeHttpRequests(auth -> auth
                        // Libera totalmente o console do H2
                        .requestMatchers("/h2-console/**").permitAll()
                        // Por enquanto, libera qualquer outra requisição para você testar sem travar no Login
                        .anyRequest().permitAll()
                )

                // Correção crucial para o Console do H2 conseguir renderizar os frames na tela
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }
}