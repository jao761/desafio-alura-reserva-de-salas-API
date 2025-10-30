package jao761.reserva_de_salas_API.model;

import java.time.LocalDate;

public class Reserva {

    protected Reserva(LocalDate inicio, LocalDate fim, Sala sala, Usuario usuario) {
        this.inicio = inicio;
        this.fim = fim;
        this.sala = sala;
        this.usuario = usuario;
        this.cancelar = false;
    }

    public Reserva() {}

    private Long id;
    private LocalDate inicio;
    private LocalDate fim;
    private Sala sala;
    private Usuario usuario;
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

    public void atualizar(LocalDate inicio, LocalDate fim) {
        this.inicio = inicio;
        this.fim = fim;
    }
}
