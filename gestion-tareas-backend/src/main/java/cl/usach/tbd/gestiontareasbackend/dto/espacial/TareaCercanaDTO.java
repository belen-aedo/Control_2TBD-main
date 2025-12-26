package cl.usach.tbd.gestiontareasbackend.dto.espacial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaCercanaDTO {
    private Long idTarea;
    private String titulo;
    private String descripcion;
    private String nombreSector;
    private Double distanciaKm;
    private LocalDate fechaVencimiento;
}