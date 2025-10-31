package jao761.reserva_de_salas_API.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario {

    public Usuario(String primeiroNome, String ultimoNome, String email, String senha) {
        this.primeiroNome = primeiroNome;
        this.ultimoNome = ultimoNome;
        this.email = email;
        this.senha = senha;
        this.ativo = true;
    }

    public Usuario() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String primeiroNome;
    @Column(nullable = false)
    private String ultimoNome;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String senha;
    @Column(nullable = false)
    private boolean ativo;
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public String getUltimoNome() {
        return ultimoNome;
    }

    public String getEmail() {
        return email;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public String getNome() {
        return getPrimeiroNome() + " " + getUltimoNome();
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void atualizarUsuario(String novoPrimeiroNome, String novoUltimoNome, String novoEmail) {
        this.primeiroNome = novoPrimeiroNome != null ? novoPrimeiroNome : this.primeiroNome;
        this.ultimoNome = novoUltimoNome != null ? novoUltimoNome : this.ultimoNome ;
        this.email = novoEmail != null ? novoEmail : this.email;
    }

    public void deletarUsuario() {
        this.ativo = false;
    }

}
