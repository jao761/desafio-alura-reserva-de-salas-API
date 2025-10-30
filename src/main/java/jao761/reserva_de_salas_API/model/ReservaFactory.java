package jao761.reserva_de_salas_API.model;

import java.time.LocalDate;

public class ReservaFactory {

    public static Reserva criarReservaComInicioFim(LocalDate inicio, LocalDate fim, Sala sala, Usuario usuario) {
         if (!fim.isAfter(inicio)) {
            throw new IllegalStateException("A data de início não pode ser posterior à data de fim!");
        }

        return new Reserva(inicio, fim, sala, usuario);
    }

    public static Reserva criarReservaComInicioFimIndeterminado(LocalDate inicio, Sala sala, Usuario usuario) {
        LocalDate fim = inicio.plusDays(1);
        return new Reserva(inicio, fim, sala, usuario);
    }
}