package br.edu.ifpb.sdatvconcorrenciaparte2.domain;

import java.util.Objects;

public class Usuario {

    private String id;
    private String nome;
    private Boolean atualizando;
    private Boolean deletando;

    public Usuario() {}

    public Usuario(String nome, Boolean atualizando, Boolean deletando) {
        this.nome = nome;
        this.atualizando = atualizando;
        this.deletando = deletando;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtualizando() {
        return atualizando;
    }

    public Boolean getDeletando() {
        return deletando;
    }

    @Override
    public String toString() {
        return "br.edu.ifpb.sdatvconcorrenciaparte2.domain.Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", atualizando=" + atualizando +
                ", deletando=" + deletando +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return getId() == usuario.getId() &&
                getNome().equals(usuario.getNome()) &&
                getAtualizando().equals(usuario.getAtualizando()) &&
                getDeletando().equals(usuario.getDeletando());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNome(), getAtualizando(), getDeletando());
    }

}
