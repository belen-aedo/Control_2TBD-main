package cl.usach.tbd.gestiontareasbackend.controller;

import cl.usach.tbd.gestiontareasbackend.dto.SectorRequest;
import cl.usach.tbd.gestiontareasbackend.dto.SectorResponse;
import cl.usach.tbd.gestiontareasbackend.service.SectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sectores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SectorController {

    private final SectorService sectorService;

    @PostMapping
    public ResponseEntity<?> crearSector(@RequestBody SectorRequest request) {
        try {
            SectorResponse sector = sectorService.crearSector(request);
            return ResponseEntity.ok(sector);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodosSectores() {
        try {
            List<SectorResponse> sectores = sectorService.obtenerTodosSectores();
            return ResponseEntity.ok(sectores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerSectorPorId(@PathVariable Long id) {
        try {
            SectorResponse sector = sectorService.obtenerSectorPorId(id);
            return ResponseEntity.ok(sector);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}