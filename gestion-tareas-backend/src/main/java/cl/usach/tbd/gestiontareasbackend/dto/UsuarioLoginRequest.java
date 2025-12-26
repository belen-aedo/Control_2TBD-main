package cl.usach.tbd.gestiontareasbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioLoginRequest {
    private String nombreUsuario;
    private String contrasena;
}