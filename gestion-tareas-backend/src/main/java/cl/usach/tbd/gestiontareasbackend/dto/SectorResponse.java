package cl.usach.tbd.gestiontareasbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorResponse {
    private Long idSector;
    private String nombre;
    private Double latitud;
    private Double longitud;
}