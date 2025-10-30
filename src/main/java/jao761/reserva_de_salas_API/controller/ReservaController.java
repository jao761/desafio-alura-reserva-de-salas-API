package jao761.reserva_de_salas_API.controller;

import jao761.reserva_de_salas_API.dto.ReservaAtualizarDTO;
import jao761.reserva_de_salas_API.dto.ReservaCadastroDTO;
import jao761.reserva_de_salas_API.dto.ReservaDetalheDTO;
import jao761.reserva_de_salas_API.model.Reserva;
import jao761.reserva_de_salas_API.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("reservas")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> fazerReserva(@RequestBody ReservaCadastroDTO dto) {
        Reserva reserva = service.fazerReserva(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reserva.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<ReservaDetalheDTO>> listarUsuarios() {
        List<ReservaDetalheDTO> reservas = service.listarPaginado();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDetalheDTO> visualizarReserva(@PathVariable Long id) {
        ReservaDetalheDTO usuarioDetalheDTO = service.visualizarReserva(id);
        return ResponseEntity.ok(usuarioDetalheDTO);
    }

    @PutMapping
    public ResponseEntity<Void> atualizarReserva(@RequestBody ReservaAtualizarDTO dto) {
        service.atualizarReserva(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {
        service.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }

}
