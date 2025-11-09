package br.com.banco.model;

import java.util.Objects;

public abstract class ContaAbstrata implements Conta {
    protected final String numero;
    protected final Cliente cliente;
    protected Float saldo;

    public ContaAbstrata(String numero, Cliente cliente, Float saldoInicial) {
        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("O número da conta não pode ser vazio.");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("O cliente da conta não pode ser nulo.");
        }
        if (saldoInicial == null || saldoInicial < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }
        this.numero = numero;
        this.cliente = cliente;
        this.saldo = saldoInicial;
    }

    @Override
    public String getNumero() {
        return numero;
    }

    @Override
    public Cliente getCliente() {
        return cliente;
    }

    @Override
    public Float getSaldo() {
        return saldo;
    }

    @Override
    public void depositar(Float valor) {
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }
        this.saldo = this.saldo + valor;
    }

    @Override
    public abstract boolean sacar(Float valor);

    @Override
    public abstract String getTipo();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContaAbstrata conta = (ContaAbstrata) o;
        return Objects.equals(numero, conta.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    // Método utilitário para serialização
    protected String toDataString() {
        return getTipo() + ";" + numero + ";" + cliente.getCpf() + ";" + String.valueOf(saldo);
    }
}
