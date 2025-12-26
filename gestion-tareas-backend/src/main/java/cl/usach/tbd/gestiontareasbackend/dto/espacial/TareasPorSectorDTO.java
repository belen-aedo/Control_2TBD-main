package cl.usach.tbd.gestiontareasbackend.dto.espacial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareasPorSectorDTO {
    private String nombreSector;
    private Long cantidadTareas;
}