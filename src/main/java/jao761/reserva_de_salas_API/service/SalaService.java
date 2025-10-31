package jao761.reserva_de_salas_API.service;

import jakarta.transaction.Transactional;
import jao761.reserva_de_salas_API.dto.SalaAtualizarDTO;
import jao761.reserva_de_salas_API.dto.SalaDTO;
import jao761.reserva_de_salas_API.dto.SalaDetalheDTO;
import jao761.reserva_de_salas_API.dto.SalaListarDTO;
import jao761.reserva_de_salas_API.model.Sala;
import jao761.reserva_de_salas_API.repository.SalaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class SalaService {

    private final SalaRepository repository;

    public SalaService(SalaRepository repository) {
        this.repository = repository;
    }

    public Sala cadastarSala(SalaDTO dto) {
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

    public Sala retornarSala(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Sala não encontrada com id: " + id));
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
}
