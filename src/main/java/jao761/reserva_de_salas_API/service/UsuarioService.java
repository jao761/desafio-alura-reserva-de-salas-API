package jao761.reserva_de_salas_API.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jao761.reserva_de_salas_API.dto.UsuarioAtualizarDTO;
import jao761.reserva_de_salas_API.dto.UsuarioCadastroDTO;
import jao761.reserva_de_salas_API.dto.UsuarioDetalheDTO;
import jao761.reserva_de_salas_API.dto.UsuarioListarDTO;
import jao761.reserva_de_salas_API.model.Usuario;
import jao761.reserva_de_salas_API.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario cadastarUsuario(UsuarioCadastroDTO dto) {
        Usuario usuario = new Usuario(dto.primeiroNome(), dto.ultimoNome(), dto.email(), dto.senha());
        repository.save(usuario);
        return usuario;
    }

    public Page<UsuarioListarDTO> listarPaginado(Pageable pageable) {
        return repository.findAllByAtivoTrue(pageable)
                .map(u -> new UsuarioListarDTO(u.getId(), u.getNome() , u.getEmail()));
    }

    public UsuarioDetalheDTO visualizarUsuario(Long id) {
        Usuario usuario = retornarUsuario(id);
        return new UsuarioDetalheDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.isAtivo());
    }

    private Usuario retornarUsuario(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado com id: " + id));
    }

    @Transactional
    public void atualizarUsuario(UsuarioAtualizarDTO dto) {
        Usuario usuario = retornarUsuario(dto.id());
        usuario.atualizarUsuario(dto.primeiroNome(), dto.ultimoNome(), dto.email());
    }

    @Transactional
    public void alteradorUsuario(Long id, boolean alterar) {
        Usuario usuario = retornarUsuario(id);
        usuario.setAtivo(alterar);
    }
}
