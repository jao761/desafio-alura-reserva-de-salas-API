package jao761.reserva_de_salas_API.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jao761.reserva_de_salas_API.dto.ReservaAtualizarDTO;
import jao761.reserva_de_salas_API.dto.ReservaCadastroDTO;
import jao761.reserva_de_salas_API.dto.ReservaDetalheDTO;
import jao761.reserva_de_salas_API.dto.ReservaListarDTO;
import jao761.reserva_de_salas_API.model.Reserva;
import jao761.reserva_de_salas_API.model.ReservaFactory;
import jao761.reserva_de_salas_API.model.Sala;
import jao761.reserva_de_salas_API.model.Usuario;
import jao761.reserva_de_salas_API.repository.ReservaRepository;
import jao761.reserva_de_salas_API.repository.SalaRepository;
import jao761.reserva_de_salas_API.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    private final ReservaRepository repository;
    private final SalaRepository salaRepository;
    private final UsuarioRepository usuarioRepository;

    public ReservaService(ReservaRepository repository, SalaRepository salaRepository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.salaRepository = salaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Reserva fazerReserva(ReservaCadastroDTO dto) {
        Sala sala = salaRepository.getReferenceById(dto.salaId());
        Usuario usuario = usuarioRepository.getReferenceById(dto.usuarioId());
        Reserva reserva;

        if (dto.fim() != null) {
            reserva = ReservaFactory.criarReservaComInicioFim(dto.inicio(), dto.fim(), sala, usuario);
        } else {
            reserva = ReservaFactory.criarReservaComInicioFimIndeterminado(dto.inicio(), sala, usuario);
        }

        boolean existeSobreposicao = repository.existeSobreposicao(reserva.getSala().getId(), reserva.getUsuario().getId(),
                reserva.getInicio(), reserva.getFim());

        if (existeSobreposicao) {
            throw new IllegalArgumentException("Usuario com data indisponivel ou sala com reserva indisponivel");
        }

        repository.save(reserva);
        return reserva;
    }

    public Page<ReservaListarDTO> listarPaginado(Pageable pageable) {
        return repository.findAllByCancelarFalse(pageable)
                .map(r -> new ReservaListarDTO(r.getId(), r.getInicio(), r.getFim(),
                        r.getSala().getId(), r.getUsuario().getId()));
    }

    public ReservaDetalheDTO visualizarReserva(Long id) {
        Reserva reserva = retornarReserva(id);
        return new ReservaDetalheDTO(reserva.getId(), reserva.getInicio(), reserva.getFim(),
                reserva.getSala().getId(), reserva.getUsuario().getId(), reserva.isCancelar());
    }

    private Reserva retornarReserva(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada com id: " + id));
    }

    @Transactional
    public void atualizarReserva(ReservaAtualizarDTO dto) {
        Reserva reserva = retornarReserva(dto.id());
        reserva.atualizar(dto.inicio(), dto.fim());
    }

    @Transactional
    public void cancelarReserva(Long id) {
        Reserva reserva = retornarReserva(id);
        reserva.setCancelar(true);
    }
}
