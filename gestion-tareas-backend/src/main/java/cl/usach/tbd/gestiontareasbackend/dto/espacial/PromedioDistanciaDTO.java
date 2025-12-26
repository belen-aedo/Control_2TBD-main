package cl.usach.tbd.gestiontareasbackend.dto.espacial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromedioDistanciaDTO {
    private Double promedioDistanciaKm;
    private Long totalCompletadas;

    // Constructor adicional para compatibilidad
    public PromedioDistanciaDTO(Double promedioDistanciaKm) {
        this.promedioDistanciaKm = promedioDistanciaKm;
        this.totalCompletadas = 0L;
    }
}