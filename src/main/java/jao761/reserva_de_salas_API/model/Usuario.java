package jao761.reserva_de_salas_API.model;

public class Usuario {

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = true;
    }

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private boolean ativo;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void atualizarUsuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public void deletarUsuario() {
        this.ativo = false;
    }

}
