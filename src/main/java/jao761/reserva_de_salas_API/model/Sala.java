package jao761.reserva_de_salas_API.model;

public class Sala {

    public Sala(String nomeSala, Integer capacidade) {
        this.nomeSala = nomeSala;
        this.capacidade = capacidade;
        this.ativo = true;
    }

    private Long id;
    private String nomeSala;
    private Integer capacidade;
    private boolean ativo;


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

    public void verificarSalaAtiva() {
        if (!this.ativo) {
            throw new IllegalStateException("A sala está inativa!");
        }
    }

    public void verificarCapacidade() {
        if (this.capacidade <= 0) {
            throw new IllegalStateException("A sala deve ter capacidade positiva!");
        }
    }

    public void atualizarDados(String novoNome, int novaCapacidade) {
        this.nomeSala = novoNome;
        this.capacidade = novaCapacidade;
    }
}
