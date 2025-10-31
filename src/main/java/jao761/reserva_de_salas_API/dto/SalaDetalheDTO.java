package jao761.reserva_de_salas_API.dto;

public record SalaDetalheDTO(

        Long id,
        String nomeSala,
        Integer capacidade,
        boolean ativo

) {
}
