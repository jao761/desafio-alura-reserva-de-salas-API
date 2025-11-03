package jao761.reserva_de_salas_API.controller;

import jakarta.validation.Valid;
import jao761.reserva_de_salas_API.dto.*;
import jao761.reserva_de_salas_API.model.Sala;
import jao761.reserva_de_salas_API.service.SalaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/salas")
public class SalaController {

    private final SalaService service;

    public SalaController(SalaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarSala(@RequestBody @Valid SalaCadastroDTO dto) {
        Sala sala = service.cadastarSala(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sala.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<Page<SalaListarDTO>> listarSalas(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<SalaListarDTO> salas = service.listarPaginado(pageable);
        return ResponseEntity.ok(salas);
    }

    @GetMapping("/{id}/reservas")
    public ResponseEntity<ReservasPorSalaDTO> viaualizarReservaPorPeriodoEmSala(@PathVariable Long id,
                                                                @RequestParam LocalDate inicio,
                                                                @RequestParam LocalDate fim) {
        ReservasPorSalaDTO reservasPorSala = service.viaualizarReservaPorPeriodoEmSala(id, inicio, fim);
        return ResponseEntity.ok(reservasPorSala);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaDetalheDTO> visualizarSala(@PathVariable Long id) {
        SalaDetalheDTO salaDetalheDTO = service.visualizarSala(id);
       return ResponseEntity.ok(salaDetalheDTO);
    }

    @PutMapping
    public ResponseEntity<Void> atualizarSala(@RequestBody @Valid SalaAtualizarDTO dto) {
        service.atualizarSala(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSala(@PathVariable Long id) {
        service.alterarSala(id, false);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> reativarSala(@PathVariable Long id) {
        service.alterarSala(id, true);
        return ResponseEntity.noContent().build();
    }

}
