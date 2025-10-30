package jao761.reserva_de_salas_API.service;

import jao761.reserva_de_salas_API.dto.UsuarioAtualizarDTO;
import jao761.reserva_de_salas_API.dto.UsuarioDTO;
import jao761.reserva_de_salas_API.dto.UsuarioDetalheDTO;
import jao761.reserva_de_salas_API.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UsuarioService {

    List<Usuario> repository = new ArrayList<>();

    public Usuario cadastarUsuario(UsuarioDTO dto) {
        Usuario usuario = new Usuario(dto.nome(), dto.email(), dto.senha());
        repository.add(usuario);
        return usuario;
    }

    public List<UsuarioDetalheDTO> listarPaginado() {
        return repository.stream()
                .map(u -> new UsuarioDetalheDTO(u.getId(), u.getNome(), u.getEmail()))
                .toList();
    }

    public UsuarioDetalheDTO visualizarUsuario(Long id) {
        Usuario usuario = retornarUsuario(id);
        return new UsuarioDetalheDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public Usuario retornarUsuario(Long id) {
        return repository.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Usuario não encontrado com id: " + id));
    }

    public void atualizarUsuario(UsuarioAtualizarDTO dto) {
        Usuario usuario = retornarUsuario(dto.id());
        usuario.atualizarUsuario(dto.nome(), dto.email());
    }

    public void deletarUsuario(Long id) {
        Usuario usuario = retornarUsuario(id);
        usuario.deletarUsuario();
    }
}
