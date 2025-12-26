package cl.usach.tbd.gestiontareasbackend.service;

import cl.usach.tbd.gestiontareasbackend.dto.SectorRequest;
import cl.usach.tbd.gestiontareasbackend.dto.SectorResponse;
import cl.usach.tbd.gestiontareasbackend.entity.Sector;
import cl.usach.tbd.gestiontareasbackend.repository.SectorRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectorService {

    private final SectorRepository sectorRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Transactional
    public SectorResponse crearSector(SectorRequest request) {
        Point ubicacion = geometryFactory.createPoint(
                new Coordinate(request.getLongitud(), request.getLatitud())
        );
        ubicacion.setSRID(4326);

        Sector sector = new Sector();
        sector.setNombre(request.getNombre());
        sector.setUbicacionEspacial(ubicacion);

        Sector guardado = sectorRepository.save(sector);
        return convertirASectorResponse(guardado);
    }

    public List<SectorResponse> obtenerTodosSectores() {
        return sectorRepository.findAll()
                .stream()
                .map(this::convertirASectorResponse)
                .collect(Collectors.toList());
    }

    public SectorResponse obtenerSectorPorId(Long id) {
        Sector sector = sectorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sector no encontrado"));
        return convertirASectorResponse(sector);
    }

    private SectorResponse convertirASectorResponse(Sector sector) {
        return new SectorResponse(
                sector.getIdSector(),
                sector.getNombre(),
                sector.getUbicacionEspacial().getY(),
                sector.getUbicacionEspacial().getX()
        );
    }
}