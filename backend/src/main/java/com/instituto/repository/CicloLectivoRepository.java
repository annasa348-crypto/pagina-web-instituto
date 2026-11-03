package com.instituto.repository;

import com.instituto.model.CicloLectivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CicloLectivoRepository extends JpaRepository<CicloLectivo, Long> {

    boolean existsByAnio(Integer anio);

    boolean existsByAnioAndIdNot(Integer anio, Long id);
}
