package cl.usach.tbd.gestiontareasbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorRequest {
    private String nombre;
    private Double latitud;
    private Double longitud;
}