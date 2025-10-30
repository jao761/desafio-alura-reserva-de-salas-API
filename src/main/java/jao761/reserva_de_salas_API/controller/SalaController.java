package jao761.reserva_de_salas_API.controller;

import jao761.reserva_de_salas_API.dto.SalaAtualizarDTO;
import jao761.reserva_de_salas_API.dto.SalaDTO;
import jao761.reserva_de_salas_API.dto.SalaDetalheDTO;
import jao761.reserva_de_salas_API.model.Sala;
import jao761.reserva_de_salas_API.service.SalaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("salas")
public class SalaController {

    private final SalaService service;

    public SalaController(SalaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarSala(@RequestBody SalaDTO dto) {
        Sala sala = service.cadastarSala(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sala.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<SalaDetalheDTO>> listarSalas() {
        List<SalaDetalheDTO> salas = service.listarPaginado();
        return ResponseEntity.ok(salas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaDetalheDTO> visualizarSala(@PathVariable Long id) {
        SalaDetalheDTO salaDetalheDTO = service.visualizarSala(id);
       return ResponseEntity.ok(salaDetalheDTO);
    }

    @PutMapping
    public ResponseEntity<Void> atualizarSala(@RequestBody SalaAtualizarDTO dto) {
        service.atualizarSala(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSala(@PathVariable Long id) {
        service.deletarSala(id);
        return ResponseEntity.noContent().build();
    }


}
