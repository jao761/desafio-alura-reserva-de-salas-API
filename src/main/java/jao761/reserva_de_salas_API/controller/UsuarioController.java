package jao761.reserva_de_salas_API.controller;

import jakarta.validation.Valid;
import jao761.reserva_de_salas_API.dto.UsuarioAtualizarDTO;
import jao761.reserva_de_salas_API.dto.UsuarioCadastroDTO;
import jao761.reserva_de_salas_API.dto.UsuarioDetalheDTO;
import jao761.reserva_de_salas_API.dto.UsuarioListarDTO;
import jao761.reserva_de_salas_API.model.Usuario;
import jao761.reserva_de_salas_API.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarUsuario(@RequestBody @Valid UsuarioCadastroDTO dto) {
        Usuario usuario = service.cadastarUsuario(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuario.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioListarDTO>> listarUsuarios(@PageableDefault(size = 10, page = 0)Pageable pageable) {
        Page<UsuarioListarDTO> usuarios = service.listarPaginado(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetalheDTO> visualizarUsuario(@PathVariable Long id) {
        UsuarioDetalheDTO usuarioDetalheDTO = service.visualizarUsuario(id);
        return ResponseEntity.ok(usuarioDetalheDTO);
    }

    @PutMapping
    public ResponseEntity<Void> atualizarUsuario(@RequestBody @Valid UsuarioAtualizarDTO dto) {
        service.atualizarUsuario(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        service.alteradorUsuario(id, false);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> reativarUsuario(@PathVariable Long id) {
        service.alteradorUsuario(id, true);
        return ResponseEntity.noContent().build();
    }

}
