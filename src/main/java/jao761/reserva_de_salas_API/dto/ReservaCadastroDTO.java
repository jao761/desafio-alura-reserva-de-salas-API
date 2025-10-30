package jao761.reserva_de_salas_API.dto;

import java.time.LocalDate;

public record ReservaCadastroDTO(

        LocalDate inicio,
        LocalDate fim,
        Long salaId,
        Long usuarioId

) {
}
