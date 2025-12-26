package cl.usach.tbd.gestiontareasbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaResponse {
    private Long idTarea;
    private String titulo;
    private String descripcion;
    private LocalDate fechaVencimiento;
    private String estado;
    private Long idUsuario;
    private String nombreUsuario;
    private Long idSector;
    private String nombreSector;
    private Double distancia; // Distancia en km desde el usuario al sector
}