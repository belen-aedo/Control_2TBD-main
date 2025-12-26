package cl.usach.tbd.gestiontareasbackend.service;

import cl.usach.tbd.gestiontareasbackend.dto.*;
import cl.usach.tbd.gestiontareasbackend.entity.Usuario;
import cl.usach.tbd.gestiontareasbackend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Transactional
    public UsuarioResponse registrarUsuario(UsuarioRegistroRequest request) {
        if (usuarioRepository.existsByNombreUsuario(request.getNombreUsuario())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        Point ubicacion = geometryFactory.createPoint(
                new Coordinate(request.getLongitud(), request.getLatitud())
        );
        ubicacion.setSRID(4326);

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(request.getNombreUsuario());
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        usuario.setUbicacionGeografica(ubicacion);

        Usuario guardado = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                guardado.getIdUsuario(),
                guardado.getNombreUsuario(),
                request.getLatitud(),
                request.getLongitud()
        );
    }

    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public UsuarioResponse obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                usuario.getUbicacionGeografica().getY(),
                usuario.getUbicacionGeografica().getX()
        );
    }
}