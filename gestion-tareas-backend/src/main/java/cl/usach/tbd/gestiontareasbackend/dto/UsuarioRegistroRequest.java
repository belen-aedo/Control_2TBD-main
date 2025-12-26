package cl.usach.tbd.gestiontareasbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRegistroRequest {
    private String nombreUsuario;
    private String contrasena;
    private Double latitud;
    private Double longitud;
}