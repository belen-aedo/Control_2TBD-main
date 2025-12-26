package cl.usach.tbd.gestiontareasbackend.repository;

import cl.usach.tbd.gestiontareasbackend.entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByUsuarioIdUsuario(Long idUsuario);

    List<Tarea> findByUsuarioIdUsuarioAndEstado(Long idUsuario, String estado);

    // Método para contar tareas por estado
    Long countByUsuarioIdUsuarioAndEstado(Long idUsuario, String estado);

    @Query("SELECT t FROM Tarea t WHERE t.usuario.idUsuario = :idUsuario " +
            "AND (LOWER(t.titulo) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.descripcion) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Tarea> buscarPorPalabraClave(@Param("idUsuario") Long idUsuario,
                                      @Param("keyword") String keyword);

    // Consulta 1: Cuántas tareas ha hecho el usuario por sector
    @Query(value = "SELECT s.nombre as nombreSector, COUNT(t.id_tarea) as cantidadTareas " +
            "FROM tarea t " +
            "INNER JOIN sector s ON t.id_sector = s.id_sector " +
            "WHERE t.id_usuario = :idUsuario " +
            "GROUP BY s.id_sector, s.nombre " +
            "ORDER BY cantidadTareas DESC",
            nativeQuery = true)
    List<Map<String, Object>> contarTareasPorSector(@Param("idUsuario") Long idUsuario);

    // Consulta 2: Tarea más cercana al usuario (pendiente) - INCLUYE FECHA
    @Query(value = "SELECT t.id_tarea, t.titulo, t.descripcion, t.fecha_vencimiento, " +
            "s.nombre as nombreSector, " +
            "ST_Distance(u.ubicacion_geografica::geography, s.ubicacion_espacial::geography) / 1000 as distanciaKm " +
            "FROM tarea t " +
            "INNER JOIN usuario u ON t.id_usuario = u.id_usuario " +
            "INNER JOIN sector s ON t.id_sector = s.id_sector " +
            "WHERE t.id_usuario = :idUsuario AND t.estado = 'PENDIENTE' " +
            "ORDER BY ST_Distance(u.ubicacion_geografica, s.ubicacion_espacial) ASC " +
            "LIMIT 1",
            nativeQuery = true)
    Map<String, Object> obtenerTareaMasCercana(@Param("idUsuario") Long idUsuario);

    // Consulta 3: Sector con más tareas completadas en un radio de 2 km
    @Query(value = "SELECT s.nombre as nombreSector, COUNT(t.id_tarea) as cantidadTareasCompletadas, " +
            "ST_Distance(u.ubicacion_geografica::geography, s.ubicacion_espacial::geography) / 1000 as distanciaKm " +
            "FROM tarea t " +
            "INNER JOIN usuario u ON t.id_usuario = u.id_usuario " +
            "INNER JOIN sector s ON t.id_sector = s.id_sector " +
            "WHERE t.id_usuario = :idUsuario " +
            "AND t.estado = 'COMPLETADA' " +
            "AND ST_DWithin(u.ubicacion_geografica::geography, s.ubicacion_espacial::geography, 2000) " +
            "GROUP BY s.id_sector, s.nombre, u.ubicacion_geografica, s.ubicacion_espacial " +
            "ORDER BY cantidadTareasCompletadas DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Map<String, Object> sectorConMasTareasEn2Km(@Param("idUsuario") Long idUsuario);

    // Consulta 4: Promedio de distancia de tareas completadas
    @Query(value = "SELECT AVG(ST_Distance(u.ubicacion_geografica::geography, s.ubicacion_espacial::geography) / 1000) as promedioDistanciaKm " +
            "FROM tarea t " +
            "INNER JOIN usuario u ON t.id_usuario = u.id_usuario " +
            "INNER JOIN sector s ON t.id_sector = s.id_sector " +
            "WHERE t.id_usuario = :idUsuario AND t.estado = 'COMPLETADA'",
            nativeQuery = true)
    Double obtenerPromedioDistanciaTareasCompletadas(@Param("idUsuario") Long idUsuario);

    // Consulta 5: Sectores con concentración de tareas pendientes
    @Query(value = "SELECT s.nombre as nombreSector, COUNT(t.id_tarea) as cantidadTareasPendientes, " +
            "ST_Y(s.ubicacion_espacial) as latitud, ST_X(s.ubicacion_espacial) as longitud " +
            "FROM tarea t " +
            "INNER JOIN sector s ON t.id_sector = s.id_sector " +
            "WHERE t.estado = 'PENDIENTE' " +
            "GROUP BY s.id_sector, s.nombre, s.ubicacion_espacial " +
            "ORDER BY cantidadTareasPendientes DESC",
            nativeQuery = true)
    List<Map<String, Object>> concentracionTareasPendientes();

    // Consulta 6: Sector con más tareas completadas en 5 km
    @Query(value = "SELECT s.nombre as nombreSector, COUNT(t.id_tarea) as cantidadTareasCompletadas, " +
            "ST_Distance(u.ubicacion_geografica::geography, s.ubicacion_espacial::geography) / 1000 as distanciaKm " +
            "FROM tarea t " +
            "INNER JOIN usuario u ON t.id_usuario = u.id_usuario " +
            "INNER JOIN sector s ON t.id_sector = s.id_sector " +
            "WHERE t.id_usuario = :idUsuario " +
            "AND t.estado = 'COMPLETADA' " +
            "AND ST_DWithin(u.ubicacion_geografica::geography, s.ubicacion_espacial::geography, 5000) " +
            "GROUP BY s.id_sector, s.nombre, u.ubicacion_geografica, s.ubicacion_espacial " +
            "ORDER BY cantidadTareasCompletadas DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Map<String, Object> sectorConMasTareasEn5Km(@Param("idUsuario") Long idUsuario);
}