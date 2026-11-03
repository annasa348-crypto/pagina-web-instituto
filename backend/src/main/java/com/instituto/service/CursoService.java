package com.instituto.service;

import com.instituto.dto.CursoDto;
import com.instituto.dto.CursoRequest;
import com.instituto.exception.RecursoNoEncontradoException;
import com.instituto.exception.ReglaNegocioException;
import com.instituto.model.CicloLectivo;
import com.instituto.model.Curso;
import com.instituto.repository.CursoRepository;
import com.instituto.repository.InscripcionRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CursoService {

    private final CursoRepository cursos;
    private final InscripcionRepository inscripciones;
    private final CicloLectivoService cicloService;

    public CursoService(CursoRepository cursos, InscripcionRepository inscripciones,
                        CicloLectivoService cicloService) {
        this.cursos = cursos;
        this.inscripciones = inscripciones;
        this.cicloService = cicloService;
    }

    private CursoDto aDto(Curso c) {
        return CursoDto.de(c, inscripciones.countByCursoId(c.getId()));
    }

    private Curso buscar(Long id) {
        return cursos.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el curso con id " + id));
    }

    // cicloId es opcional: sin él se listan todos los cursos
    public List<CursoDto> listar(Long cicloId) {
        List<Curso> lista = (cicloId == null)
                ? cursos.findAll(Sort.by("nombre"))
                : cursos.findByCicloLectivoIdOrderByNombre(cicloId);
        return lista.stream().map(this::aDto).toList();
    }

    public CursoDto obtener(Long id) {
        return aDto(buscar(id));
    }

    @Transactional
    public CursoDto crear(CursoRequest req) {
        CicloLectivo ciclo = cicloService.obtener(req.cicloLectivoId());
        String nombre = req.nombre().trim();
        // Regla: el nombre del curso no puede repetirse dentro del mismo ciclo lectivo
        if (cursos.existsByNombreIgnoreCaseAndCicloLectivoId(nombre, ciclo.getId())) {
            throw new ReglaNegocioException(
                    "Ya existe un curso llamado '" + nombre + "' en el ciclo " + ciclo.getAnio());
        }
        Curso c = new Curso();
        c.setNombre(nombre);
        c.setCapacidadMaxima(req.capacidadMaxima());
        c.setCicloLectivo(ciclo);
        return aDto(cursos.save(c));
    }

    @Transactional
    public CursoDto actualizar(Long id, CursoRequest req) {
        Curso c = buscar(id);
        CicloLectivo ciclo = cicloService.obtener(req.cicloLectivoId());
        String nombre = req.nombre().trim();
        long inscriptos = inscripciones.countByCursoId(id);

        if (cursos.existsByNombreIgnoreCaseAndCicloLectivoIdAndIdNot(nombre, ciclo.getId(), id)) {
            throw new ReglaNegocioException(
                    "Ya existe otro curso llamado '" + nombre + "' en el ciclo " + ciclo.getAnio());
        }
        // Regla: la capacidad no puede quedar por debajo de los alumnos ya inscriptos
        if (req.capacidadMaxima() < inscriptos) {
            throw new ReglaNegocioException(
                    "La capacidad no puede ser menor a los alumnos inscriptos (" + inscriptos + ")");
        }
        // Regla: un curso con inscripciones no puede pasar a otro ciclo lectivo
        if (!c.getCicloLectivo().getId().equals(ciclo.getId()) && inscriptos > 0) {
            throw new ReglaNegocioException(
                    "No se puede cambiar el ciclo lectivo de un curso que ya tiene inscripciones");
        }
        c.setNombre(nombre);
        c.setCapacidadMaxima(req.capacidadMaxima());
        c.setCicloLectivo(ciclo);
        return aDto(cursos.save(c));
    }

    @Transactional
    public void eliminar(Long id) {
        Curso c = buscar(id);
        // Regla: no se elimina un curso con inscripciones registradas
        if (inscripciones.existsByCursoId(id)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar el curso porque tiene alumnos inscriptos");
        }
        cursos.delete(c);
    }
}
