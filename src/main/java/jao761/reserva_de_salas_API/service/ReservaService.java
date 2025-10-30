package jao761.reserva_de_salas_API.service;

import jao761.reserva_de_salas_API.dto.*;
import jao761.reserva_de_salas_API.model.Reserva;
import jao761.reserva_de_salas_API.model.ReservaFactory;
import jao761.reserva_de_salas_API.model.Sala;
import jao761.reserva_de_salas_API.model.Usuario;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservaService {

    private final SalaService salaService;
    private final UsuarioService usuarioService;
    List<Reserva> repository = new ArrayList<>();

    public ReservaService(SalaService salaService, UsuarioService usuarioService) {
        this.salaService = salaService;
        this.usuarioService = usuarioService;
    }

    public Reserva fazerReserva(ReservaCadastroDTO dto) {
        Sala sala = salaService.retornarSala(dto.salaId());
        Usuario usuario = usuarioService.retornarUsuario(dto.usuarioId());
        Reserva reserva;

        if (dto.fim() != null) {
            reserva = ReservaFactory.criarReservaComInicioFim(dto.inicio(), dto.fim(), sala, usuario);
        } else {
            reserva = ReservaFactory.criarReservaComInicioFimIndeterminado(dto.inicio(), sala, usuario);
        }

        validarSobreposicaoSala(reserva);
        validarSobreposicaoUsuario(reserva);
        repository.add(reserva);
        return reserva;
    }

    public List<ReservaDetalheDTO> listarPaginado() {
        return repository.stream()
                .map(r -> new ReservaDetalheDTO(r.getId(), r.getInicio(), r.getFim(),
                        r.getSala().getId(), r.getUsuario().getId()))
                .toList();
    }

    public ReservaDetalheDTO visualizarReserva(Long id) {
        Reserva reserva = retornarReserva(id);
        return new ReservaDetalheDTO(reserva.getId(), reserva.getInicio(), reserva.getFim(),
                reserva.getSala().getId(), reserva.getUsuario().getId());
    }

    public Reserva retornarReserva(Long id) {
        return repository.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Reserva não encontrada com id: " + id));
    }

    public void atualizarReserva(ReservaAtualizarDTO dto) {
        Reserva reserva = retornarReserva(dto.id());
        reserva.atualizar(dto.inicio(), dto.fim());
    }

    public void cancelarReserva(Long id) {
        Reserva reserva = retornarReserva(id);
        reserva.setCancelar(true);
    }

    public void validarSobreposicaoSala(Reserva reserva) {
        boolean existeSobreposicao = repository.stream()
                .filter(r -> r.getSala().equals(reserva.getSala()) && !r.isCancelar())
                .anyMatch(r ->
                        reserva.getInicio().isBefore(r.getFim()) && reserva.getFim().isAfter(r.getInicio())
                );

        if (existeSobreposicao) {
            throw new IllegalStateException("Esta data não está disponível");
        }
    }

    public void validarSobreposicaoUsuario(Reserva reserva) {
        boolean exiteSobreposicaoUsuario = repository.stream()
                .filter(r -> r.getUsuario().equals(reserva.getUsuario()) && !r.isCancelar())
                .anyMatch(r ->
                        reserva.getInicio().isBefore(r.getFim()) && reserva.getFim().isAfter(r.getInicio())
                );

        if (exiteSobreposicaoUsuario) {
            throw new IllegalStateException("Usuario ja tem data marcada para esse diax'");
        }
    }



}
