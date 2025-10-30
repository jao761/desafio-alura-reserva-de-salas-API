package jao761.reserva_de_salas_API.controller;

import jao761.reserva_de_salas_API.dto.UsuarioAtualizarDTO;
import jao761.reserva_de_salas_API.dto.UsuarioDTO;
import jao761.reserva_de_salas_API.dto.UsuarioDetalheDTO;
import jao761.reserva_de_salas_API.model.Usuario;
import jao761.reserva_de_salas_API.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarUsuario(@RequestBody UsuarioDTO dto) {
        Usuario usuario = service.cadastarUsuario(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuario.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDetalheDTO>> listarUsuarios() {
        List<UsuarioDetalheDTO> usuarios = service.listarPaginado();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetalheDTO> visualizarUsuario(@PathVariable Long id) {
        UsuarioDetalheDTO usuarioDetalheDTO = service.visualizarUsuario(id);
        return ResponseEntity.ok(usuarioDetalheDTO);
    }

    @PutMapping
    public ResponseEntity<Void> atualizarUsuario(@RequestBody UsuarioAtualizarDTO dto) {
        service.atualizarUsuario(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        service.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
