package jao761.reserva_de_salas_API.dto;

import jakarta.validation.constraints.NotBlank;

public record SalaCadastroDTO(

    @NotBlank String nomeSala,
    @NotBlank Integer capacidade

) {
}
