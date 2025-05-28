package com.example.projek_pbo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/upload").hasRole("ADMIN")
                .requestMatchers("/anggota/**").hasRole("ANGGOTA")
                .requestMatchers("/home", "/register", "/login", "/css/**", "/images/**", "/cover/**").permitAll()
                .anyRequest().authenticated() // Semua permintaan lainnya harus diautentikasi
            )
            .formLogin(form -> form
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .successHandler((request, response, authentication) -> {
                    authentication.getAuthorities().forEach(authority -> {
                        try {
                            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                                response.sendRedirect("/dashboard?success=true");
                            } else if (authority.getAuthority().equals("ROLE_ANGGOTA")) {
                                response.sendRedirect("/home?success=true");
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // URL untuk logout
                .logoutSuccessUrl("/home") // Redirect setelah logout sukses
                .invalidateHttpSession(true) // Menghapus sesi
                .deleteCookies("JSESSIONID") // Menghapus cookie JSESSIONID
                .permitAll()
            )
            .csrf().disable(); // Aktifkan jika perlu

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}