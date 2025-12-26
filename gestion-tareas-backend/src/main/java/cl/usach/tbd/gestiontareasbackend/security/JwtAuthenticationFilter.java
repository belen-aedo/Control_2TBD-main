package cl.usach.tbd.gestiontareasbackend.security;

import cl.usach.tbd.gestiontareasbackend.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        System.out.println("🔐 JwtFilter - Path: " + request.getRequestURI());
        System.out.println("🔐 JwtFilter - Authorization Header: " + (authHeader != null ? "Present" : "Missing"));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("🔐 JwtFilter - No Bearer token found, continuing without auth");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try {
            username = jwtService.extractUsername(jwt);
            System.out.println("🔐 JwtFilter - Username extracted: " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Verificamos que el usuario exista en la BD
                usuarioService.buscarPorNombreUsuario(username);
                System.out.println("🔐 JwtFilter - Usuario encontrado en BD");

                if (jwtService.isTokenValid(jwt, username)) {
                    System.out.println("🔐 JwtFilter - Token válido, autenticando usuario");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            new ArrayList<>() // Aquí irían los roles si los tuvieras
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.out.println("🔐 JwtFilter - Token inválido o expirado");
                }
            }
        } catch (Exception e) {
            System.out.println("🔐 JwtFilter - Error al procesar token: " + e.getMessage());
            // Si el token es inválido o el usuario no existe, no autenticamos
            // El request sigue su curso y SecurityConfig lo rechazará con 403
        }

        filterChain.doFilter(request, response);
    }
}