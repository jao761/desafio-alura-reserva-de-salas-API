package jao761.reserva_de_salas_API.dto;

import java.util.List;

public record ReservasPorSalaDTO(

        Long salaId,
        List<ReservaPeriodoDTO> reservas

) {
}
