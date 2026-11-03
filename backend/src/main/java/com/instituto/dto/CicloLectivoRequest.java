package com.instituto.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CicloLectivoRequest(
        @NotNull(message = "El año es obligatorio")
        @Min(value = 2000, message = "El año no puede ser anterior a 2000")
        @Max(value = 2100, message = "El año no puede ser posterior a 2100") Integer anio) {
}
