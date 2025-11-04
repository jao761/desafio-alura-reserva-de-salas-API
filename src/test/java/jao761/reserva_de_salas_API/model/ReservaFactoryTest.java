package jao761.reserva_de_salas_API.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservaFactoryTest {

    @Test
    @DisplayName("Deve criar reserva com início e fim válidos, adicionando 1 dia ao fim")
    void deveCriarReservaComInicioFimValidos() {
        // Arrange
        LocalDate inicio = LocalDate.of(2025, 11, 1);
        LocalDate fim = LocalDate.of(2025, 11, 3);
        Sala sala = new Sala("Sala de Reunião", 10);
        Usuario usuario = new Usuario();

        // Act
        Reserva reserva = ReservaFactory.criarReservaComInicioFim(inicio, fim, sala, usuario);

        // Assert
        assertEquals(inicio, reserva.getInicio());
        assertEquals(fim.plusDays(1), reserva.getFim());
        assertEquals(sala, reserva.getSala());
        assertEquals(usuario, reserva.getUsuario());
    }

    @Test
    @DisplayName("Deve lançar exceção se a data de início for posterior à data de fim")
    void deveLancarExcecaoSeDataFimForInvalida() {
        // Arrange
        LocalDate inicio = LocalDate.of(2025, 11, 3);
        LocalDate fim = LocalDate.of(2025, 11, 1);
        Sala sala = new Sala("Sala 1", 5);
        Usuario usuario = new Usuario();

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ReservaFactory.criarReservaComInicioFim(inicio, fim, sala, usuario)
        );

        assertEquals("A data de início não pode ser posterior à data de fim!", ex.getMessage());
    }

    @Test
    @DisplayName("Deve criar reserva com fim indeterminado, adicionando 1 dia ao início")
    void deveCriarReservaComFimIndeterminado() {
        // Arrange
        LocalDate inicio = LocalDate.of(2025, 11, 10);
        Sala sala = new Sala("Auditório", 30);
        Usuario usuario = new Usuario();

        // Act
        Reserva reserva = ReservaFactory.criarReservaComInicioFimIndeterminado(inicio, sala, usuario);

        // Assert
        assertEquals(inicio, reserva.getInicio());
        assertEquals(inicio.plusDays(1), reserva.getFim());
        assertEquals(sala, reserva.getSala());
        assertEquals(usuario, reserva.getUsuario());
    }
}