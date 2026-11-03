package com.instituto.controller;

import com.instituto.dto.CursoDto;
import com.instituto.dto.CursoRequest;
import com.instituto.service.CursoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }

    // GET /api/cursos            -> todos
    // GET /api/cursos?cicloId=1  -> solo los de un ciclo lectivo
    @GetMapping
    public List<CursoDto> listar(@RequestParam(required = false) Long cicloId) {
        return service.listar(cicloId);
    }

    @GetMapping("/{id}")
    public CursoDto obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CursoDto crear(@Valid @RequestBody CursoRequest req) {
        return service.crear(req);
    }

    @PutMapping("/{id}")
    public CursoDto actualizar(@PathVariable Long id, @Valid @RequestBody CursoRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
