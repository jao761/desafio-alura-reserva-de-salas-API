package jao761.reserva_de_salas_API.controller;

import jakarta.validation.Valid;
import jao761.reserva_de_salas_API.dto.ReservaAtualizarDTO;
import jao761.reserva_de_salas_API.dto.ReservaCadastroDTO;
import jao761.reserva_de_salas_API.dto.ReservaDetalheDTO;
import jao761.reserva_de_salas_API.dto.ReservaListarDTO;
import jao761.reserva_de_salas_API.model.Reserva;
import jao761.reserva_de_salas_API.service.ReservaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/reservas")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> fazerReserva(@RequestBody @Valid ReservaCadastroDTO dto) {
        Reserva reserva = service.fazerReserva(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reserva.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<Page<ReservaListarDTO>> listarUsuarios(@PageableDefault(size = 10, page = 0)Pageable pageable) {
        Page<ReservaListarDTO> reservas = service.listarPaginado(pageable);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDetalheDTO> visualizarReserva(@PathVariable Long id) {
        ReservaDetalheDTO usuarioDetalheDTO = service.visualizarReserva(id);
        return ResponseEntity.ok(usuarioDetalheDTO);
    }

    @PutMapping
    public ResponseEntity<Void> atualizarReserva(@RequestBody @Valid ReservaAtualizarDTO dto) {
        service.atualizarReserva(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {
        service.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }

}
