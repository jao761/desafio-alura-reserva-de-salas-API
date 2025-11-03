package jao761.reserva_de_salas_API.dto;

import jakarta.validation.constraints.NotNull;

public record SalaAtualizarDTO (

        @NotNull Long id,
        String nomeSala,
        Integer capacidade

){
}
