package com.instituto.dto;

import com.instituto.model.Curso;

public record CursoDto(
        Long id,
        String nombre,
        Integer capacidadMaxima,
        Long cicloLectivoId,
        Integer anio,
        long inscriptos,
        long cuposDisponibles) {

    public static CursoDto de(Curso c, long inscriptos) {
        return new CursoDto(
                c.getId(),
                c.getNombre(),
                c.getCapacidadMaxima(),
                c.getCicloLectivo().getId(),
                c.getCicloLectivo().getAnio(),
                inscriptos,
                Math.max(0, c.getCapacidadMaxima() - inscriptos));
    }
}
