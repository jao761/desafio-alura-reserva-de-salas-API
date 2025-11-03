package jao761.reserva_de_salas_API.dto;

import jakarta.validation.constraints.NotNull;

public record UsuarioAtualizarDTO(

        @NotNull Long id,
        String primeiroNome,
        String ultimoNome,
        String email

) {
}
