package jao761.reserva_de_salas_API.service;

import jao761.reserva_de_salas_API.dto.*;
import jao761.reserva_de_salas_API.model.Sala;
import jao761.reserva_de_salas_API.repository.ReservaRepository;
import jao761.reserva_de_salas_API.repository.SalaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private SalaService salaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve cadastrar uma nova sala")
    void deveCadastrarSala() {
        // Arrange
        SalaCadastroDTO dto = new SalaCadastroDTO("Sala 101", 20);

        // Act
        Sala sala = salaService.cadastarSala(dto);

        // Assert
        assertNotNull(sala);
        assertEquals("Sala 101", sala.getNomeSala());
        assertEquals(20, sala.getCapacidade());
        verify(salaRepository, times(1)).save(any(Sala.class));
    }

    @Test
    @DisplayName("Deve listar salas ativas paginadas")
    void deveListarSalasAtivasPaginadas() {
        // Arrange
        Sala sala = new Sala("Sala 202", 10);
        sala.setId(1L);
        Page<Sala> page = new PageImpl<>(List.of(sala));
        when(salaRepository.findAllByAtivoTrue(any(Pageable.class))).thenReturn(page);

        // Act
        Page<SalaListarDTO> result = salaService.listarPaginado(Pageable.unpaged());

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Sala 202", result.getContent().get(0).nomeSala());
        assertEquals(10, result.getContent().get(0).capacidade());
    }

    @Test
    @DisplayName("Deve visualizar sala por ID com sucesso")
    void deveVisualizarSalaPorId() {
        // Arrange
        Sala sala = new Sala("Sala 303", 15);
        sala.setId(3L);
        when(salaRepository.findById(3L)).thenReturn(Optional.of(sala));

        // Act
        SalaDetalheDTO dto = salaService.visualizarSala(3L);

        // Assert
        assertEquals(3L, dto.id());
        assertEquals("Sala 303", dto.nomeSala());
        assertEquals(15, dto.capacidade());
        assertTrue(dto.ativo());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar visualizar sala não encontrada")
    void deveLancarExcecaoQuandoSalaNaoEncontrada() {
        // Arrange
        when(salaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> salaService.visualizarSala(99L));
        assertEquals("Sala não encontrada com id: 99", ex.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar o nome e a capacidade de uma sala")
    void deveAtualizarSala() {
        // Arrange
        Sala sala = new Sala("Sala Antiga", 5);
        sala.setId(1L);
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));

        SalaAtualizarDTO dto = new SalaAtualizarDTO(1L, "Sala Nova", 10);

        // Act
        salaService.atualizarSala(dto);

        // Assert
        assertEquals("Sala Nova", sala.getNomeSala());
        assertEquals(10, sala.getCapacidade());
    }

    @Test
    @DisplayName("Deve ativar uma sala desativada")
    void deveAtivarSala() {
        // Arrange
        Sala sala = new Sala("Sala 404", 8);
        sala.setId(4L);
        sala.setAtivo(false);
        when(salaRepository.findById(4L)).thenReturn(Optional.of(sala));

        // Act
        salaService.alterarSala(4L, true);

        // Assert
        assertTrue(sala.isAtivo());
    }

    @Test
    @DisplayName("Deve desativar uma sala ativada")
    void deveDesativarSala() {
        // Arrange
        Sala sala = new Sala("Sala 505", 12);
        sala.setId(5L);
        sala.setAtivo(true);
        when(salaRepository.findById(5L)).thenReturn(Optional.of(sala));

        // Act
        salaService.alterarSala(5L, false);

        // Assert
        assertFalse(sala.isAtivo());
    }

    @Test
    @DisplayName("Deve retornar reservas em uma sala dentro do período especificado")
    void deveRetornarReservasPorPeriodoEmSala() {
        // Arrange
        Long salaId = 1L;
        LocalDate inicio = LocalDate.of(2025, 11, 1);
        LocalDate fim = LocalDate.of(2025, 11, 5);
        List<ReservaPeriodoDTO> reservas = List.of(
                new ReservaPeriodoDTO(inicio, fim)
        );
        when(reservaRepository.findAllBySalaIdInPeriodo(salaId, inicio, fim)).thenReturn(reservas);

        // Act
        ReservasPorSalaDTO dto = salaService.viaualizarReservaPorPeriodoEmSala(salaId, inicio, fim);

        // Assert
        assertEquals(salaId, dto.salaId());
        assertEquals(1, dto.reservas().size());
    }

    @Test
    @DisplayName("Deve lançar exceção quando período de consulta for maior que 30 dias")
    void deveLancarExcecaoQuandoPeriodoMaiorQue30Dias() {
        // Arrange
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fim = LocalDate.of(2025, 2, 10);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                salaService.viaualizarReservaPorPeriodoEmSala(1L, inicio, fim));

        assertEquals("O período não pode ser superior a 30 dias", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar sala não encontrada")
    void atualizarSala_quandoNaoEncontrada_deveLancarExcecao() {
        // Arrange
        SalaAtualizarDTO dto = new SalaAtualizarDTO(100L, "Sala X", 30);
        when(salaRepository.findById(100L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> salaService.atualizarSala(dto));
        assertEquals("Sala não encontrada com id: 100", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar alterar status de sala não encontrada")
    void alterarSala_quandoNaoEncontrada_deveLancarExcecao() {
        // Arrange
        when(salaRepository.findById(200L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> salaService.alterarSala(200L, true));
        assertEquals("Sala não encontrada com id: 200", ex.getMessage());
    }

    @Test
    @DisplayName("Deve retornar página vazia ao listar salas sem resultados")
    void listarPaginado_quandoListaVazia_deveRetornarVazio() {
        // Arrange
        when(salaRepository.findAllByAtivoTrue(any(Pageable.class))).thenReturn(Page.empty());

        // Act
        Page<SalaListarDTO> page = salaService.listarPaginado(Pageable.unpaged());

        // Assert
        assertTrue(page.isEmpty());
    }

    @Test
    @DisplayName("Não deve alterar sala se o status for o mesmo")
    void alterarSala_semMudancaDeStatus_naoDeveAlterar() {
        // Arrange
        Sala sala = new Sala("Sala 707", 50);
        sala.setId(7L);
        sala.setAtivo(true);
        when(salaRepository.findById(7L)).thenReturn(Optional.of(sala));

        // Act
        salaService.alterarSala(7L, true);

        // Assert
        assertTrue(sala.isAtivo());
        verify(salaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve salvar sala com capacidade zero")
    void cadastrarSala_comCapacidadeZero_aindaDeveSalvar() {
        // Arrange
        SalaCadastroDTO dto = new SalaCadastroDTO("Sala Vazia", 0);

        // Act
        Sala sala = salaService.cadastarSala(dto);

        // Assert
        assertEquals(0, sala.getCapacidade());
        verify(salaRepository).save(any(Sala.class));
    }

    @Test
    @DisplayName("Deve retornar lista vazia de reservas se não houverem no período")
    void visualizarReservaPorPeriodo_semReservas_deveRetornarListaVazia() {
        // Arrange
        Long salaId = 1L;
        LocalDate inicio = LocalDate.now();
        LocalDate fim = inicio.plusDays(5);
        when(reservaRepository.findAllBySalaIdInPeriodo(salaId, inicio, fim)).thenReturn(List.of());

        // Act
        ReservasPorSalaDTO dto = salaService.viaualizarReservaPorPeriodoEmSala(salaId, inicio, fim);

        // Assert
        assertEquals(0, dto.reservas().size());
    }
}