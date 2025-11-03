package jao761.reserva_de_salas_API.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Sala {

    public Sala(String nomeSala, Integer capacidade) {
        this.nomeSala = nomeSala;
        this.capacidade = capacidade;
        this.ativo = true;
    }

    public Sala() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nomeSala;
    @Column(nullable = false)
    private Integer capacidade;
    @Column(nullable = false)
    private boolean ativo;
    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public String getNomeSala() {
        return nomeSala;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void verificarSalaAtiva() {
        if (!this.ativo) {
            throw new IllegalArgumentException("A sala está inativa!");
        }
    }

    public void verificarCapacidade() {
        if (this.capacidade <= 0) {
            throw new IllegalArgumentException("A sala deve ter capacidade positiva!");
        }
    }

    public void atualizarDados(String novoNome, Integer novaCapacidade) {
        this.nomeSala = novoNome != null ? novoNome : this.getNomeSala() ;
        this.capacidade = novaCapacidade != null ? novaCapacidade : this.getCapacidade();
    }
}
