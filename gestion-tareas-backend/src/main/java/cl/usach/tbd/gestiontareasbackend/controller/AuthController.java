package cl.usach.tbd.gestiontareasbackend.controller;

import cl.usach.tbd.gestiontareasbackend.dto.*;
import cl.usach.tbd.gestiontareasbackend.entity.Usuario;
import cl.usach.tbd.gestiontareasbackend.security.JwtService;
import cl.usach.tbd.gestiontareasbackend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioRegistroRequest request) {
        try {
            UsuarioResponse usuario = usuarioService.registrarUsuario(request);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioLoginRequest request) {
        try {
            System.out.println("=== INICIO LOGIN ===");
            System.out.println("Usuario recibido: " + request.getNombreUsuario());
            System.out.println("Contraseña recibida: " + request.getContrasena());

            Usuario usuario = usuarioService.buscarPorNombreUsuario(request.getNombreUsuario());
            System.out.println("Usuario encontrado en BD: " + usuario.getNombreUsuario());
            System.out.println("Hash en BD: " + usuario.getContrasena());

            boolean matches = passwordEncoder.matches(request.getContrasena(), usuario.getContrasena());
            System.out.println("¿Contraseña coincide?: " + matches);

            if (!matches) {
                System.out.println("Credenciales inválidas - contraseña no coincide");
                return ResponseEntity.badRequest().body("Credenciales inválidas");
            }

            String token = jwtService.generateToken(usuario.getNombreUsuario(), usuario.getIdUsuario());
            System.out.println("Token generado exitosamente");

            UsuarioResponse usuarioResponse = new UsuarioResponse(
                    usuario.getIdUsuario(),
                    usuario.getNombreUsuario(),
                    usuario.getUbicacionGeografica().getY(),
                    usuario.getUbicacionGeografica().getX()
            );

            LoginResponse response = new LoginResponse(token, usuarioResponse);
            System.out.println("=== LOGIN EXITOSO ===");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("=== ERROR EN LOGIN ===");
            System.out.println("Tipo de error: " + e.getClass().getName());
            System.out.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Credenciales inválidas");
        }
    }

    @PostMapping("/hash-password")
    public ResponseEntity<?> hashPassword(@RequestBody String password) {
        try {
            String hash = passwordEncoder.encode(password);
            return ResponseEntity.ok(hash);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al generar hash");
        }
    }
}