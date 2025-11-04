package jao761.reserva_de_salas_API.service;

import jakarta.persistence.EntityNotFoundException;
import jao761.reserva_de_salas_API.dto.*;
import jao761.reserva_de_salas_API.model.Reserva;
import jao761.reserva_de_salas_API.model.ReservaFactory;
import jao761.reserva_de_salas_API.model.Sala;
import jao761.reserva_de_salas_API.model.Usuario;
import jao761.reserva_de_salas_API.repository.ReservaRepository;
import jao761.reserva_de_salas_API.repository.SalaRepository;
import jao761.reserva_de_salas_API.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaServiceTest {

    @Mock
    private ReservaRepository repository;
    @Mock
    private SalaRepository salaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private ReservaService reservaService;

    private Sala sala;
    private Usuario usuario;
    private Reserva reserva;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sala = new Sala("Sala Reunião", 10);
        usuario = new Usuario("João", "Pontel", "joao@email.com", "123456");
        reserva = ReservaFactory.criarReservaComInicioFim(
                LocalDate.of(2025, 11, 1),
                LocalDate.of(2025, 11, 3),
                sala,
                usuario
        );
    }

    @Test
    @DisplayName("Deve criar uma reserva com sucesso quando não há sobreposição")
    void deveFazerReservaComSucesso() {
        // Arrange
        ReservaCadastroDTO dto = new ReservaCadastroDTO(LocalDate.of(2025, 11, 1),
                LocalDate.of(2025, 11, 3), 1L, 1L);

        when(salaRepository.getReferenceById(1L)).thenReturn(sala);
        when(usuarioRepository.getReferenceById(1L)).thenReturn(usuario);
        when(repository.existeSobreposicao(anyLong(), anyLong(), any(), any())).thenReturn(false);
        when(repository.save(any(Reserva.class))).thenReturn(reserva);

        // Act
        Reserva resultado = reservaService.fazerReserva(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(sala, resultado.getSala());
        assertEquals(usuario, resultado.getUsuario());
        verify(repository).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando houver sobreposição de reserva")
    void deveLancarExcecaoQuandoHouverSobreposicao() {
        // Arrange
        ReservaCadastroDTO dto = new ReservaCadastroDTO(LocalDate.of(2025, 11, 1),
                LocalDate.of(2025, 11, 3), 1L, 1L);

        sala.setId(1L);
        usuario.setId(1L);

        when(salaRepository.getReferenceById(1L)).thenReturn(sala);
        when(usuarioRepository.getReferenceById(1L)).thenReturn(usuario);
        when(repository.existeSobreposicao(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reservaService.fazerReserva(dto));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar reservas paginadas com sucesso")
    void deveListarReservasPaginadas() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 5);
        when(repository.findAllByCancelarFalse(pageable))
                .thenReturn(new PageImpl<>(List.of(reserva)));

        // Act
        Page<ReservaListarDTO> resultado = reservaService.listarPaginado(pageable);

        // Assert
        assertEquals(1, resultado.getTotalElements());
        assertEquals(reserva.getId(), resultado.getContent().get(0).id());
        verify(repository).findAllByCancelarFalse(pageable);
    }

    @Test
    @DisplayName("Deve retornar detalhes da reserva corretamente")
    void deveVisualizarReservaPorId() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(reserva));

        // Act
        ReservaDetalheDTO dto = reservaService.visualizarReserva(1L);

        // Assert
        assertNotNull(dto);
        assertEquals(reserva.getSala().getId(), dto.salaId());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando reserva não for encontrada")
    void deveLancarExcecaoQuandoReservaNaoForEncontrada() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> reservaService.visualizarReserva(1L));
    }

    @Test
    @DisplayName("Deve atualizar reserva corretamente")
    void deveAtualizarReserva() {
        // Arrange
        ReservaAtualizarDTO dto = new ReservaAtualizarDTO(1L, LocalDate.of(2025, 11, 2), LocalDate.of(2025, 11, 4));
        when(repository.findById(1L)).thenReturn(Optional.of(reserva));

        // Act
        reservaService.atualizarReserva(dto);

        // Assert
        assertEquals(LocalDate.of(2025, 11, 2), reserva.getInicio());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve cancelar reserva com sucesso")
    void deveCancelarReserva() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(reserva));

        // Act
        reservaService.cancelarReserva(1L);

        // Assert
        assertTrue(reserva.isCancelar());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Não deve salvar reserva se ocorrer exceção inesperada")
    void naoDeveSalvarReservaSeOcorrerErro() {
        // Arrange
        ReservaCadastroDTO dto = new ReservaCadastroDTO(LocalDate.of(2025, 11, 1),
                LocalDate.of(2025, 11, 3), 1L, 1L);

        when(salaRepository.getReferenceById(1L)).thenReturn(sala);
        when(usuarioRepository.getReferenceById(1L)).thenReturn(usuario);
        when(repository.existeSobreposicao(anyLong(), anyLong(), any(), any())).thenReturn(false);
        when(repository.save(any())).thenThrow(new RuntimeException("Erro no banco"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> reservaService.fazerReserva(dto));
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Deve criar reserva com sucesso mesmo com data de fim nula (indeterminada)")
    void deveCriarReservaComFimIndeterminado() {
        // Arrange
        ReservaCadastroDTO dto = new ReservaCadastroDTO(LocalDate.of(2025, 11, 1),
                null, 1L, 1L);
        Reserva reservaIndeterminada = ReservaFactory.criarReservaComInicioFimIndeterminado(
                LocalDate.of(2025, 11, 1), sala, usuario);

        when(salaRepository.getReferenceById(1L)).thenReturn(sala);
        when(usuarioRepository.getReferenceById(1L)).thenReturn(usuario);
        when(repository.existeSobreposicao(anyLong(), anyLong(), any(), any())).thenReturn(false);
        when(repository.save(any(Reserva.class))).thenReturn(reservaIndeterminada);

        // Act
        Reserva resultado = reservaService.fazerReserva(dto);

        // Assert
        assertNotNull(resultado);
        verify(repository).save(any());
    }
}