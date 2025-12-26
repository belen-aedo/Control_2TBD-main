package cl.usach.tbd.gestiontareasbackend.service;

import cl.usach.tbd.gestiontareasbackend.dto.*;
import cl.usach.tbd.gestiontareasbackend.dto.espacial.*;
import cl.usach.tbd.gestiontareasbackend.entity.Sector;
import cl.usach.tbd.gestiontareasbackend.entity.Tarea;
import cl.usach.tbd.gestiontareasbackend.entity.Usuario;
import cl.usach.tbd.gestiontareasbackend.repository.SectorRepository;
import cl.usach.tbd.gestiontareasbackend.repository.TareaRepository;
import cl.usach.tbd.gestiontareasbackend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SectorRepository sectorRepository;

    @Transactional
    public TareaResponse crearTarea(Long idUsuario, TareaRequest request) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Sector sector = sectorRepository.findById(request.getIdSector())
                .orElseThrow(() -> new RuntimeException("Sector no encontrado"));

        Tarea tarea = new Tarea();
        tarea.setTitulo(request.getTitulo());
        tarea.setDescripcion(request.getDescripcion());
        tarea.setFechaVencimiento(request.getFechaVencimiento());
        tarea.setEstado("PENDIENTE");
        tarea.setUsuario(usuario);
        tarea.setSector(sector);

        Tarea guardada = tareaRepository.save(tarea);
        return convertirATareaResponse(guardada);
    }

    @Transactional
    public TareaResponse actualizarTarea(Long idTarea, TareaRequest request) {
        Tarea tarea = tareaRepository.findById(idTarea)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        tarea.setTitulo(request.getTitulo());
        tarea.setDescripcion(request.getDescripcion());
        tarea.setFechaVencimiento(request.getFechaVencimiento());

        if (request.getIdSector() != null) {
            Sector sector = sectorRepository.findById(request.getIdSector())
                    .orElseThrow(() -> new RuntimeException("Sector no encontrado"));
            tarea.setSector(sector);
        }

        Tarea actualizada = tareaRepository.save(tarea);
        return convertirATareaResponse(actualizada);
    }

    @Transactional
    public void eliminarTarea(Long idTarea) {
        if (!tareaRepository.existsById(idTarea)) {
            throw new RuntimeException("Tarea no encontrada");
        }
        tareaRepository.deleteById(idTarea);
    }

    @Transactional
    public TareaResponse marcarComoCompletada(Long idTarea) {
        Tarea tarea = tareaRepository.findById(idTarea)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        tarea.setEstado("COMPLETADA");
        Tarea actualizada = tareaRepository.save(tarea);
        return convertirATareaResponse(actualizada);
    }

    public List<TareaResponse> obtenerTareasDeUsuario(Long idUsuario) {
        return tareaRepository.findByUsuarioIdUsuario(idUsuario)
                .stream()
                .map(this::convertirATareaResponse)
                .collect(Collectors.toList());
    }

    public TareaResponse obtenerTareaPorId(Long idTarea) {
        Tarea tarea = tareaRepository.findById(idTarea)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        return convertirATareaResponse(tarea);
    }

    public List<TareaResponse> obtenerTareasPorEstado(Long idUsuario, String estado) {
        return tareaRepository.findByUsuarioIdUsuarioAndEstado(idUsuario, estado)
                .stream()
                .map(this::convertirATareaResponse)
                .collect(Collectors.toList());
    }

    public List<TareaResponse> buscarTareasPorPalabraClave(Long idUsuario, String keyword) {
        return tareaRepository.buscarPorPalabraClave(idUsuario, keyword)
                .stream()
                .map(this::convertirATareaResponse)
                .collect(Collectors.toList());
    }

    public List<TareaResponse> obtenerTareasProximasAVencer(Long idUsuario, int dias) {
        LocalDate fechaLimite = LocalDate.now().plusDays(dias);
        return tareaRepository.findByUsuarioIdUsuarioAndEstado(idUsuario, "PENDIENTE")
                .stream()
                .filter(t -> !t.getFechaVencimiento().isAfter(fechaLimite))
                .map(this::convertirATareaResponse)
                .collect(Collectors.toList());
    }

    // Consultas espaciales

    public List<TareasPorSectorDTO> contarTareasPorSector(Long idUsuario) {
        List<Map<String, Object>> resultados = tareaRepository.contarTareasPorSector(idUsuario);
        return resultados.stream()
                .map(r -> new TareasPorSectorDTO(
                        (String) r.get("nombresector"),
                        ((Number) r.get("cantidadtareas")).longValue()
                ))
                .collect(Collectors.toList());
    }

    public TareaCercanaDTO obtenerTareaMasCercana(Long idUsuario) {
        Map<String, Object> resultado = tareaRepository.obtenerTareaMasCercana(idUsuario);
        if (resultado == null || resultado.isEmpty()) {
            return null;
        }

        // Manejar la fecha de vencimiento
        LocalDate fechaVencimiento = null;
        Object fechaObj = resultado.get("fecha_vencimiento");
        if (fechaObj != null) {
            if (fechaObj instanceof LocalDate) {
                fechaVencimiento = (LocalDate) fechaObj;
            } else if (fechaObj instanceof java.sql.Date) {
                fechaVencimiento = ((java.sql.Date) fechaObj).toLocalDate();
            } else if (fechaObj instanceof java.util.Date) {
                fechaVencimiento = new java.sql.Date(((java.util.Date) fechaObj).getTime()).toLocalDate();
            }
        }

        return new TareaCercanaDTO(
                ((Number) resultado.get("id_tarea")).longValue(),
                (String) resultado.get("titulo"),
                (String) resultado.get("descripcion"),
                (String) resultado.get("nombresector"),
                ((Number) resultado.get("distanciakm")).doubleValue(),
                fechaVencimiento
        );
    }

    public SectorConTareasDTO sectorConMasTareasEn2Km(Long idUsuario) {
        Map<String, Object> resultado = tareaRepository.sectorConMasTareasEn2Km(idUsuario);
        if (resultado == null || resultado.isEmpty()) {
            return null;
        }
        return new SectorConTareasDTO(
                (String) resultado.get("nombresector"),
                ((Number) resultado.get("cantidadtareascompletadas")).longValue(),
                ((Number) resultado.get("distanciakm")).doubleValue()
        );
    }

    public PromedioDistanciaDTO obtenerPromedioDistancia(Long idUsuario) {
        Double promedio = tareaRepository.obtenerPromedioDistanciaTareasCompletadas(idUsuario);
        Long totalCompletadas = tareaRepository.countByUsuarioIdUsuarioAndEstado(idUsuario, "COMPLETADA");

        return new PromedioDistanciaDTO(
                promedio != null ? promedio : 0.0,
                totalCompletadas != null ? totalCompletadas : 0L
        );
    }

    public List<ConcentracionTareasDTO> concentracionTareasPendientes() {
        List<Map<String, Object>> resultados = tareaRepository.concentracionTareasPendientes();
        return resultados.stream()
                .map(r -> new ConcentracionTareasDTO(
                        (String) r.get("nombresector"),
                        ((Number) r.get("cantidadtareaspendientes")).longValue(),
                        ((Number) r.get("latitud")).doubleValue(),
                        ((Number) r.get("longitud")).doubleValue()
                ))
                .collect(Collectors.toList());
    }

    public SectorConTareasDTO sectorConMasTareasEn5Km(Long idUsuario) {
        Map<String, Object> resultado = tareaRepository.sectorConMasTareasEn5Km(idUsuario);
        if (resultado == null || resultado.isEmpty()) {
            return null;
        }
        return new SectorConTareasDTO(
                (String) resultado.get("nombresector"),
                ((Number) resultado.get("cantidadtareascompletadas")).longValue(),
                ((Number) resultado.get("distanciakm")).doubleValue()
        );
    }

    private TareaResponse convertirATareaResponse(Tarea tarea) {
        // Calcular distancia entre usuario y sector
        Double distancia = calcularDistancia(
                tarea.getUsuario().getUbicacionGeografica(),
                tarea.getSector().getUbicacionEspacial()
        );
        
        return new TareaResponse(
                tarea.getIdTarea(),
                tarea.getTitulo(),
                tarea.getDescripcion(),
                tarea.getFechaVencimiento(),
                tarea.getEstado(),
                tarea.getUsuario().getIdUsuario(),
                tarea.getUsuario().getNombreUsuario(),
                tarea.getSector().getIdSector(),
                tarea.getSector().getNombre(),
                distancia
        );
    }

    /**
     * Calcula la distancia en kilómetros entre dos puntos usando la fórmula de Haversine
     */
    private Double calcularDistancia(org.locationtech.jts.geom.Point punto1, org.locationtech.jts.geom.Point punto2) {
        if (punto1 == null || punto2 == null) {
            return null;
        }

        double lat1 = punto1.getY();
        double lon1 = punto1.getX();
        double lat2 = punto2.getY();
        double lon2 = punto2.getX();

        final double RADIO_TIERRA_KM = 6371.0;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return RADIO_TIERRA_KM * c;
    }
}