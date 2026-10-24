package com.kodigo.bank.security;

import com.kodigo.bank.entity.Atm;
import com.kodigo.bank.repository.AtmRepository;
import com.kodigo.bank.util.Hashing;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;

// Autenticacion maquina-a-maquina de los cajeros: cada peticion trae
// X-ATM-ID (codigo del cajero) y X-ATM-KEY (su llave secreta).
// No es un @Component a proposito: solo se registra en la cadena de /api/atm/**.
public class AtmAuthenticationFilter extends OncePerRequestFilter {
    public static final String ATM_ID_HEADER = "X-ATM-ID";
    public static final String ATM_KEY_HEADER = "X-ATM-KEY";

    private final AtmRepository atmRepository;

    public AtmAuthenticationFilter(AtmRepository atmRepository) {
        this.atmRepository = atmRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String atmId = request.getHeader(ATM_ID_HEADER);
        String key = request.getHeader(ATM_KEY_HEADER);
        if (atmId != null && key != null) {
            Optional<Atm> atm = atmRepository.findByAtmCode(atmId).filter(Atm::isActive);
            if (atm.isPresent() && MessageDigest.isEqual(
                    atm.get().getApiKeyHash().getBytes(StandardCharsets.UTF_8),
                    Hashing.sha256(key).getBytes(StandardCharsets.UTF_8))) {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        atm.get().getAtmCode(), null, List.of(new SimpleGrantedAuthority("ROLE_ATM"))));
            }
        }
        chain.doFilter(request, response);
    }
}
