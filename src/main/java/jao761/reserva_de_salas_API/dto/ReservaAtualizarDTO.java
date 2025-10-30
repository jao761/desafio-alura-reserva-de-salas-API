package jao761.reserva_de_salas_API.dto;

import java.time.LocalDate;

public record ReservaAtualizarDTO(

        Long id,
        LocalDate inicio,
        LocalDate fim

) {
}
