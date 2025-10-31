package jao761.reserva_de_salas_API.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Reserva {

    protected Reserva(LocalDate inicio, LocalDate fim, Sala sala, Usuario usuario) {
        this.inicio = inicio;
        this.fim = fim;
        this.sala = sala;
        this.usuario = usuario;
        this.cancelar = false;
    }

    public Reserva() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate inicio;
    @Column(nullable = false)
    private LocalDate fim;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Sala sala;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Usuario usuario;
    @Column(nullable = false)
    private boolean cancelar;

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() { return usuario; }

    public LocalDate getInicio() {
        return inicio;
    }

    public LocalDate getFim() {
        return fim;
    }

    public Sala getSala() {
        return sala;
    }

    public void setCancelar(boolean cancelar) { this.cancelar = cancelar; }

    public boolean isCancelar() {
        return cancelar;
    }

    public void atualizar(LocalDate novoInicio, LocalDate novoFim) {
        this.inicio = novoInicio != null ? novoInicio : this.inicio;
        this.fim = novoFim != null ? novoFim.plusDays(1) : this.fim;
        if (!this.fim.isAfter(this.inicio)) {
            throw new IllegalStateException("A data de início não pode ser posterior à data de fim!");
        }
    }
}
