package com.iurimedina.validacao_clientes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.authorizeHttpRequests(auth -> auth
                .requestMatchers("/clientes/**").hasRole("ADMIN")
                .requestMatchers("/validacao-chave-cliente/**").hasRole("VALIDACAO")
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable);
        
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        
        UserDetails admin = User.builder()
            .username("iuri-medina")
            .password(encoder.encode("validacao@cliente")) 
            .roles("ADMIN")
            .build();
        
        UserDetails validacao = User.builder()
        		.username("importador-oferta")
        		.password(encoder.encode("importador@oferta"))
        		.roles("VALIDACAO")
        		.build();
        
        return new InMemoryUserDetailsManager(admin, validacao);
    }
}