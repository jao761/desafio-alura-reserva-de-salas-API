package jao761.reserva_de_salas_API.service;

import jao761.reserva_de_salas_API.dto.SalaAtualizarDTO;
import jao761.reserva_de_salas_API.dto.SalaDTO;
import jao761.reserva_de_salas_API.dto.SalaDetalheDTO;
import jao761.reserva_de_salas_API.model.Sala;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SalaService {

    List<Sala> repository = new ArrayList<>();

    public Sala cadastarSala(SalaDTO dto) {
        Sala sala = new Sala(dto.nomeSala(), dto.capacidade());
        repository.add(sala);
        return sala;
    }

    public List<SalaDetalheDTO> listarPaginado() {
        return repository.stream()
                .map(s -> new SalaDetalheDTO(s.getId(), s.getNomeSala(), s.getCapacidade()))
                .toList();
    }

    public SalaDetalheDTO visualizarSala(Long id) {
        Sala sala = retornarSala(id);
        return new SalaDetalheDTO(sala.getId(), sala.getNomeSala(), sala.getCapacidade());
    }

    public Sala retornarSala(Long id) {
        return repository.stream()
                .filter(sala -> sala.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Sala não encontrada com id: " + id));
    }

    public void atualizarSala(SalaAtualizarDTO dto) {
        Sala sala = retornarSala(dto.id());
        sala.atualizarDados(dto.nomeSala(), dto.capacidade());
    }

    public void deletarSala(Long id) {
        Sala sala = retornarSala(id);
        sala.setAtivo(false);
    }
}
