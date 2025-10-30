package jao761.reserva_de_salas_API.dto;

import java.time.LocalDate;

public record ReservaDetalheDTO(

        Long id,
        LocalDate inicio,
        LocalDate fim,
        Long salaId,
        Long usuarioId

) {
}
