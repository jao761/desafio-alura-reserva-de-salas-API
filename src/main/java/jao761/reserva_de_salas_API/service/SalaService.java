package jao761.reserva_de_salas_API.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jao761.reserva_de_salas_API.dto.*;
import jao761.reserva_de_salas_API.model.Reserva;
import jao761.reserva_de_salas_API.model.Sala;
import jao761.reserva_de_salas_API.repository.ReservaRepository;
import jao761.reserva_de_salas_API.repository.SalaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SalaService {

    private final SalaRepository repository;
    private final ReservaRepository reservaRepository;

    public SalaService(SalaRepository repository, ReservaRepository reservaRepository) {
        this.repository = repository;
        this.reservaRepository = reservaRepository;
    }


    public Sala cadastarSala(SalaCadastroDTO dto) {
        Sala sala = new Sala(dto.nomeSala(), dto.capacidade());
        repository.save(sala);
        return sala;
    }

    public Page<SalaListarDTO> listarPaginado(Pageable pageable) {
        return repository.findAllByAtivoTrue(pageable)
                .map(s -> new SalaListarDTO(s.getId(), s.getNomeSala(), s.getCapacidade()));
    }

    public SalaDetalheDTO visualizarSala(Long id) {
        Sala sala = retornarSala(id);
        return new SalaDetalheDTO(sala.getId(), sala.getNomeSala(), sala.getCapacidade(), sala.isAtivo());
    }

    private Sala retornarSala(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sala não encontrada com id: " + id));
    }

    @Transactional
    public void atualizarSala(SalaAtualizarDTO dto) {
        Sala sala = retornarSala(dto.id());
        sala.atualizarDados(dto.nomeSala(), dto.capacidade());
    }

    @Transactional
    public void alterarSala(Long id, boolean alterador) {
        Sala sala = retornarSala(id);
        sala.setAtivo(alterador);
    }

    public ReservasPorSalaDTO viaualizarReservaPorPeriodoEmSala(Long id, LocalDate inicio, LocalDate fim) {
        long dias = ChronoUnit.DAYS.between(inicio, fim);
        if (dias > 30)
            throw new IllegalArgumentException("O período não pode ser superior a 30 dias");
        return new ReservasPorSalaDTO(id, reservaRepository.findAllBySalaIdInPeriodo(id, inicio, fim));
    }
}
