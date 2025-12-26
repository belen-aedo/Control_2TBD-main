package cl.usach.tbd.gestiontareasbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaRequest {
    private String titulo;
    private String descripcion;
    private LocalDate fechaVencimiento;
    private Long idSector;
}