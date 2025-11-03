package jao761.reserva_de_salas_API.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservaCadastroDTO(

        @NotNull LocalDate inicio,
        LocalDate fim,
        @NotNull Long salaId,
        @NotNull Long usuarioId

) {
}
