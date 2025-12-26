package cl.usach.tbd.gestiontareasbackend.repository;

import cl.usach.tbd.gestiontareasbackend.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {
}