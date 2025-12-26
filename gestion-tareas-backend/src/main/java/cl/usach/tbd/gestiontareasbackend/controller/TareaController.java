package cl.usach.tbd.gestiontareasbackend.controller;

import cl.usach.tbd.gestiontareasbackend.dto.*;
import cl.usach.tbd.gestiontareasbackend.dto.espacial.*;
import cl.usach.tbd.gestiontareasbackend.security.JwtService;
import cl.usach.tbd.gestiontareasbackend.service.TareaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TareaController {

    private final TareaService tareaService;
    private final JwtService jwtService;

    private Long obtenerIdUsuarioDesdeToken(String authHeader) {
        String token = authHeader.substring(7);
        return jwtService.extractUserId(token);
    }

    @PostMapping
    public ResponseEntity<?> crearTarea(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TareaRequest request
    ) {
        try {
            Long idUsuario = obtenerIdUsuarioDesdeToken(authHeader);
            TareaResponse tarea = tareaService.crearTarea(idUsuario, request);
            return ResponseEntity.ok(tarea);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTarea(
            @PathVariable Long id,
            @RequestBody TareaRequest request
    ) {
        try {
            TareaResponse tarea = tareaService.actualizarTarea(id, request);
            return ResponseEntity.ok(tarea);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTarea(@PathVariable Long id) {
        try {
            tareaService.eliminarTarea(id);
            return ResponseEntity.ok().body("Tarea eliminada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<?> marcarComoCompletada(@PathVariable Long id) {
        try {
            TareaResponse tarea = tareaService.marcarComoCompletada(id);
            return ResponseEntity.ok(tarea);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerTareas(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            Long idUsuario = obtenerIdUsuarioDesdeToken(authHeader);
            List<TareaResponse> tareas = tareaService.obtenerTareasDeUsuario(idUsuario);
            return ResponseEntity.ok(tareas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> obtenerTareasPorEstado(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String estado
    ) {
        try {
            Long idUsuario = obtenerIdUsuarioDesdeToken(authHeader);
            List<TareaResponse> tareas = tareaService.obtenerTareasPorEstado(idUsuario, estado);
            return ResponseEntity.ok(tareas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarTareas(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String keyword
    ) {
        try {
            Long idUsuario = obtenerIdUsuarioDesdeToken(authHeader);
            List<TareaResponse> tareas = tareaService.buscarTareasPorPalabraClave(idUsuario, keyword);
            return ResponseEntity.ok(tareas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/proximas-vencer")
    public ResponseEntity<?> obtenerTareasProximasAVencer(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(defaultValue = "7") int dias
    ) {
        try {
            Long idUsuario = obtenerIdUsuarioDesdeToken(authHeader);
            List<TareaResponse> tareas = tareaService.obtenerTareasProximasAVencer(idUsuario, dias);
            return ResponseEntity.ok(tareas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoints para consultas espaciales

    @GetMapping("/analisis/por-sector")
    public ResponseEntity<?> contarTareasPorSector(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            Long idUsuario = obtenerIdUsuarioDesdeToken(authHeader);
            List<TareasPorSectorDTO> resultado = tareaService.contarTareasPorSector(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/analisis/mas-cercana")
    public ResponseEntity<?> obtenerTareaMasCercana(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            Long idUsuario = obtenerIdUsuarioDesdeToken(authHeader);
            TareaCercanaDTO resultado = tareaService.obtenerTareaMasCercana(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/analisis/sector-mas-tareas-2km")
    public ResponseEntity<?> sectorConMasTareasEn2Km(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            Long idUsuario = obtenerIdUsuarioDesdeToken(authHeader);
            SectorConTareasDTO resultado = tareaService.sectorConMasTareasEn2Km(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/analisis/promedio-distancia")
    public ResponseEntity<?> obtenerPromedioDistancia(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            Long idUsuario = obtenerIdUsuarioDesdeToken(authHeader);
            PromedioDistanciaDTO resultado = tareaService.obtenerPromedioDistancia(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/analisis/concentracion-pendientes")
    public ResponseEntity<?> concentracionTareasPendientes() {
        try {
            List<ConcentracionTareasDTO> resultado = tareaService.concentracionTareasPendientes();
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/analisis/sector-mas-tareas-5km")
    public ResponseEntity<?> sectorConMasTareasEn5Km(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            Long idUsuario = obtenerIdUsuarioDesdeToken(authHeader);
            SectorConTareasDTO resultado = tareaService.sectorConMasTareasEn5Km(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}