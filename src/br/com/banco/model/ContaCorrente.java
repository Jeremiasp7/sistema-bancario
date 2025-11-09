package br.com.banco.model;

public class ContaCorrente extends ContaAbstrata {

    public static final String TIPO = "CORRENTE";

    public ContaCorrente(String numero, Cliente cliente, Float saldoInicial) {
        super(numero, cliente, saldoInicial);
    }

    @Override
    public boolean sacar(Float valor) {
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser positivo.");
        }
        if (this.saldo.compareTo(valor) >= 0) {
            this.saldo = this.saldo - valor;
            return true;
        }
        return false; // Rejeitar saque se saldo insuficiente
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public String toString() {
        return toDataString();
    }

    public static ContaCorrente fromString(String data, Cliente cliente) {
        String[] parts = data.split(";");
        if (parts.length != 4 || !parts[0].equals(TIPO)) {
            throw new IllegalArgumentException("Formato de dados de Conta Corrente inválido.");
        }
        String numero = parts[1];
        // parts[2] é o CPF, já usado para buscar o cliente
        Float saldo = Float.valueOf(parts[3]);
        return new ContaCorrente(numero, cliente, saldo);
    }
}
