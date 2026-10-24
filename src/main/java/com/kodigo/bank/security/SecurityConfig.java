package com.kodigo.bank.security;

import com.kodigo.bank.repository.AtmRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // API de cajeros: sin sesion ni cookies, cada peticion se autentica con su llave.
    // CSRF no aplica porque no hay cookie de sesion que un sitio externo pueda aprovechar.
    @Bean
    @Order(1)
    public SecurityFilterChain atmApi(HttpSecurity http, AtmRepository atmRepository) throws Exception {
        http.securityMatcher("/api/atm/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new AtmAuthenticationFilter(atmRepository), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth.anyRequest().hasRole("ATM"))
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        return http.build();
    }

    // Sitio web y API de clientes/administradores: login con formulario, sesion y CSRF.
    @Bean
    @Order(2)
    public SecurityFilterChain web(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/atm", "/css/**", "/img/**", "/js/**", "/error").permitAll()
                        .requestMatchers("/api/v1/account/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/v1/**", "/admin", "/bank-loan", "/loan-payment", "/loan-management",
                                "/card-management", "/card-payment", "/credit-card").hasRole("ADMIN")
                        .requestMatchers("/index", "/").authenticated()
                        .anyRequest().hasRole("CUSTOMER"))
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/index", true)
                        .permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login?logout"))
                // Las llamadas a la API sin sesion reciben 401 en vez de la pagina de login
                .exceptionHandling(e -> e.defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/api/**")));
        return http.build();
    }
}
