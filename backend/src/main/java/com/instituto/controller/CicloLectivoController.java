package com.instituto.controller;

import com.instituto.dto.CicloLectivoRequest;
import com.instituto.model.CicloLectivo;
import com.instituto.service.CicloLectivoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ciclos-lectivos")
public class CicloLectivoController {

    private final CicloLectivoService service;

    public CicloLectivoController(CicloLectivoService service) {
        this.service = service;
    }

    @GetMapping
    public List<CicloLectivo> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public CicloLectivo obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CicloLectivo crear(@Valid @RequestBody CicloLectivoRequest req) {
        return service.crear(req);
    }

    @PutMapping("/{id}")
    public CicloLectivo actualizar(@PathVariable Long id, @Valid @RequestBody CicloLectivoRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
