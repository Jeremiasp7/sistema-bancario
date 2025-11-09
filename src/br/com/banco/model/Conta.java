package br.com.banco.model;

public interface Conta {
    String getNumero();
    Cliente getCliente();
    Float getSaldo();
    void depositar(Float valor);
    boolean sacar(Float valor);
    String getTipo();
}
