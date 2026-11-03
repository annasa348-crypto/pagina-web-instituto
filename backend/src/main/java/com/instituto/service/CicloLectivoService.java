package com.instituto.service;

import com.instituto.dto.CicloLectivoRequest;
import com.instituto.exception.RecursoNoEncontradoException;
import com.instituto.exception.ReglaNegocioException;
import com.instituto.model.CicloLectivo;
import com.instituto.repository.CicloLectivoRepository;
import com.instituto.repository.CursoRepository;
import com.instituto.repository.MatriculaRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CicloLectivoService {

    private final CicloLectivoRepository ciclos;
    private final CursoRepository cursos;
    private final MatriculaRepository matriculas;

    public CicloLectivoService(CicloLectivoRepository ciclos, CursoRepository cursos,
                               MatriculaRepository matriculas) {
        this.ciclos = ciclos;
        this.cursos = cursos;
        this.matriculas = matriculas;
    }

    public List<CicloLectivo> listar() {
        return ciclos.findAll(Sort.by("anio"));
    }

    public CicloLectivo obtener(Long id) {
        return ciclos.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el ciclo lectivo con id " + id));
    }

    @Transactional
    public CicloLectivo crear(CicloLectivoRequest req) {
        // Regla: no puede haber dos ciclos lectivos del mismo año
        if (ciclos.existsByAnio(req.anio())) {
            throw new ReglaNegocioException("Ya existe el ciclo lectivo " + req.anio());
        }
        CicloLectivo c = new CicloLectivo();
        c.setAnio(req.anio());
        return ciclos.save(c);
    }

    @Transactional
    public CicloLectivo actualizar(Long id, CicloLectivoRequest req) {
        CicloLectivo c = obtener(id);
        if (ciclos.existsByAnioAndIdNot(req.anio(), id)) {
            throw new ReglaNegocioException("Ya existe el ciclo lectivo " + req.anio());
        }
        c.setAnio(req.anio());
        return ciclos.save(c);
    }

    @Transactional
    public void eliminar(Long id) {
        CicloLectivo c = obtener(id);
        // Regla: no se elimina un ciclo que ya tiene cursos o matrículas (historial)
        if (cursos.existsByCicloLectivoId(id)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar el ciclo " + c.getAnio() + " porque tiene cursos asociados");
        }
        if (matriculas.existsByCicloLectivoId(id)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar el ciclo " + c.getAnio() + " porque tiene matrículas asociadas");
        }
        ciclos.delete(c);
    }
}
