package br.com.banco.service;

import br.com.banco.model.Cliente;
import br.com.banco.model.Conta;
import br.com.banco.model.ContaCorrente;
import br.com.banco.model.ContaPoupanca;
import br.com.banco.repository.RepositorioConta;

import java.util.Optional;

public class ServicoConta {

    private final RepositorioConta repositorioConta;
    private final ServicoCliente servicoCliente;

    public ServicoConta(RepositorioConta repositorioConta, ServicoCliente servicoCliente) {
        this.repositorioConta = repositorioConta;
        this.servicoCliente = servicoCliente;
    }

    public Conta cadastrarConta(String numero, String cpfCliente, Float saldoInicial, String tipo) {
        if (repositorioConta.buscarPorNumero(numero).isPresent()) {
            throw new IllegalArgumentException("Conta com número " + numero + " já cadastrada.");
        }

        Optional<Cliente> clienteOpt = servicoCliente.buscarClientePorCpf(cpfCliente);
        if (clienteOpt.isEmpty()) {
            throw new IllegalArgumentException("Cliente com CPF " + cpfCliente + " não encontrado.");
        }
        Cliente cliente = clienteOpt.get();

        Conta novaConta;
        if (ContaCorrente.TIPO.equalsIgnoreCase(tipo)) {
            novaConta = new ContaCorrente(numero, cliente, saldoInicial);
        } else if (ContaPoupanca.TIPO.equalsIgnoreCase(tipo)) {
            novaConta = new ContaPoupanca(numero, cliente, saldoInicial);
        } else {
            throw new IllegalArgumentException("Tipo de conta inválido: " + tipo);
        }

        repositorioConta.salvar(novaConta);
        return novaConta;
    }

    public boolean contaExiste(String numero) {
        return repositorioConta.buscarPorNumero(numero).isPresent();
    }

    public void depositar(String numeroConta, Float valor) {
        Conta conta = repositorioConta.buscarPorNumero(numeroConta)
                .orElseThrow(() -> new IllegalArgumentException("Conta " + numeroConta + " não encontrada."));

        conta.depositar(valor);
        repositorioConta.salvar(conta);
    }

    public boolean sacar(String numeroConta, Float valor) {
        Conta conta = repositorioConta.buscarPorNumero(numeroConta)
                .orElseThrow(() -> new IllegalArgumentException("Conta " + numeroConta + " não encontrada."));

        boolean sucesso = conta.sacar(valor);
        if (sucesso) {
            repositorioConta.salvar(conta); 
        }
        return sucesso;
    }

    public boolean transferir(String numeroContaOrigem, String numeroContaDestino, Float valor) {
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
        }

        Conta contaOrigem = repositorioConta.buscarPorNumero(numeroContaOrigem)
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem " + numeroContaOrigem + " não encontrada."));

        Conta contaDestino = repositorioConta.buscarPorNumero(numeroContaDestino)
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino " + numeroContaDestino + " não encontrada."));

        boolean saqueSucesso = contaOrigem.sacar(valor);

        if (saqueSucesso) {
            contaDestino.depositar(valor);

            repositorioConta.salvar(contaOrigem);
            repositorioConta.salvar(contaDestino);
            return true;
        }

        return false; 
    }

    public Optional<Conta> buscarContaPorNumero(String numero) {
        return repositorioConta.buscarPorNumero(numero);
    }
}
