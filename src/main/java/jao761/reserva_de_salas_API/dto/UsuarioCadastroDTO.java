package jao761.reserva_de_salas_API.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioCadastroDTO(

        @NotBlank String primeiroNome,
        @NotBlank String ultimoNome,
        @NotBlank String email,
        @NotBlank String senha

) {
}
