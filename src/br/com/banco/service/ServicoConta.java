package br.com.banco.service;

import br.com.banco.model.Cliente;
import br.com.banco.model.Conta;
import br.com.banco.model.ContaCorrente;
import br.com.banco.model.ContaPoupanca;
import br.com.banco.repository.RepositorioConta;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Float consultarSaldo(String numeroConta) {
        Conta conta = repositorioConta.buscarPorNumero(numeroConta)
                .orElseThrow(() -> new IllegalArgumentException("Conta " + numeroConta + " não encontrada."));
        return conta.getSaldo();
    }

    public void aplicarRendimentoPoupanca(Float percentual) {
        if (percentual == null || percentual <= 0) {
            throw new IllegalArgumentException("O percentual de rendimento deve ser positivo.");
        }

        List<Conta> contas = repositorioConta.buscarTodos();
        for (Conta conta : contas) {
            if (conta.getTipo().equalsIgnoreCase("POUPANCA")) {
                Float saldoAtual = conta.getSaldo();
                Float rendimento = saldoAtual * (percentual / 100);
                conta.depositar(rendimento);
                repositorioConta.salvar(conta);
            }
        }
    }

    public List<Conta> listarContasOrdenadasPorSaldo() {
        return repositorioConta.buscarTodos().stream()
                .sorted((c1, c2) -> c2.getSaldo().compareTo(c1.getSaldo()))
                .collect(Collectors.toList());
    }

    public void gerarRelatorioConsolidacao() {
        List<Conta> contas = repositorioConta.buscarTodos();

        long totalCorrente = contas.stream().filter(c -> c.getTipo().equalsIgnoreCase("CORRENTE")).count();
        long totalPoupanca = contas.stream().filter(c -> c.getTipo().equalsIgnoreCase("POUPANCA")).count();

        float saldoCorrente = contas.stream()
                .filter(c -> c.getTipo().equalsIgnoreCase("CORRENTE"))
                .map(Conta::getSaldo)
                .reduce(0f, Float::sum);

        float saldoPoupanca = contas.stream()
                .filter(c -> c.getTipo().equalsIgnoreCase("POUPANCA"))
                .map(Conta::getSaldo)
                .reduce(0f, Float::sum);

        float saldoTotal = saldoCorrente + saldoPoupanca;
        long totalContas = totalCorrente + totalPoupanca;

        System.out.println("\n--- Relatório de Consolidação ---");
        System.out.println("Contas Corrente: " + totalCorrente + " | Saldo total: R$ " + saldoCorrente);
        System.out.println("Contas Poupança: " + totalPoupanca + " | Saldo total: R$ " + saldoPoupanca);
        System.out.println("--------------------------------");
        System.out.println("Total de contas: " + totalContas);
        System.out.println("Saldo total do banco: R$ " + saldoTotal);
    }
    
    public Optional<Conta> buscarContaPorNumero(String numero) {
        return repositorioConta.buscarPorNumero(numero);
    }
}
