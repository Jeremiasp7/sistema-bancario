package br.com.banco.model;

import java.util.Objects;

public class Cliente {
    private final String nome;
    private final String cpf;

    public Cliente(String nome, String cpf) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do cliente não pode ser vazio.");
        }
        if (cpf == null || !cpf.matches("\\d{11}")) { 
            throw new IllegalArgumentException("O CPF do cliente deve conter 11 dígitos.");
        }
        this.nome = nome;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(cpf, cliente.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    @Override
    public String toString() {
        return nome + ";" + cpf;
    }

    public static Cliente fromString(String data) {
        String[] parts = data.split(";");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Formato de dados de cliente inválido.");
        }
        return new Cliente(parts[0], parts[1]);
    }
}
