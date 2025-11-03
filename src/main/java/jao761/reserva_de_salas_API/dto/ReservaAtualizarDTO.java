package jao761.reserva_de_salas_API.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservaAtualizarDTO(

        @NotNull Long id,
        LocalDate inicio,
        LocalDate fim

) {
}
