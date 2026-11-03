package com.instituto.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CursoRequest(
        @NotBlank(message = "El nombre del curso es obligatorio")
        @Size(max = 50, message = "El nombre no puede superar los 50 caracteres") String nombre,
        @NotNull(message = "La capacidad máxima es obligatoria")
        @Min(value = 1, message = "La capacidad máxima debe ser al menos 1")
        @Max(value = 200, message = "La capacidad máxima no puede superar 200") Integer capacidadMaxima,
        @NotNull(message = "El ciclo lectivo es obligatorio") Long cicloLectivoId) {
}
