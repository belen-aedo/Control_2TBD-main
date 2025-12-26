package cl.usach.tbd.gestiontareasbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Entity
@Table(name = "sector")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sector")
    private Long idSector;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "ubicacion_espacial", columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point ubicacionEspacial;

    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL)
    private List<Tarea> tareas;
}