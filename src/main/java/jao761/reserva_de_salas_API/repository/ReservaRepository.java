package jao761.reserva_de_salas_API.repository;

import jakarta.persistence.LockModeType;
import jao761.reserva_de_salas_API.dto.ReservaPeriodoDTO;
import jao761.reserva_de_salas_API.model.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM Reserva r
        WHERE r.cancelar = false
          AND (
              (r.sala.id = :salaId) 
              OR 
              (r.usuario.id = :usuarioId)
          )
          AND (
              (:inicio < r.fim AND :fim > r.inicio)
          )
    """)
    boolean existeSobreposicao(Long salaId, Long usuarioId, LocalDate inicio, LocalDate fim);

    Page<Reserva> findAllByCancelarFalse(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT new jao761.reserva_de_salas_API.dto.ReservaPeriodoDTO(r.inicio, r.fim)
    FROM Reserva r
    WHERE r.sala.id = :salaId
      AND r.inicio <= :fim
      AND r.fim >= :inicio
    """)
    List<ReservaPeriodoDTO> findAllBySalaIdInPeriodo(Long salaId, LocalDate inicio, LocalDate fim
    );

}
