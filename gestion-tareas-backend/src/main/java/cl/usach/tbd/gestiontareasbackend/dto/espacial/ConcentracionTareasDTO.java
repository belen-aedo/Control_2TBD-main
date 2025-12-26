package cl.usach.tbd.gestiontareasbackend.dto.espacial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcentracionTareasDTO {
    private String nombreSector;
    private Long cantidadTareasPendientes;
    private Double latitud;
    private Double longitud;
}