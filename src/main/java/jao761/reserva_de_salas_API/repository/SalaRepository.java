package jao761.reserva_de_salas_API.repository;

import jao761.reserva_de_salas_API.model.Sala;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    public Page<Sala> findAllByAtivoTrue(Pageable pageable);
}
