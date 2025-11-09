package br.com.banco;

import br.com.banco.model.Conta;
import br.com.banco.repository.RepositorioClienteArquivo;
import br.com.banco.repository.RepositorioContaArquivo;
import br.com.banco.service.ServicoCliente;
import br.com.banco.service.ServicoConta;

import java.util.Scanner;

public class Main {

    private static ServicoCliente servicoCliente;
    private static ServicoConta servicoConta;
    private static Scanner sc;

    public static void main(String[] args) {
        RepositorioClienteArquivo repositorioCliente = new RepositorioClienteArquivo();
        RepositorioContaArquivo repositorioConta = new RepositorioContaArquivo(repositorioCliente);

        servicoCliente = new ServicoCliente(repositorioCliente);
        servicoConta = new ServicoConta(repositorioConta, servicoCliente);
        sc = new Scanner(System.in);

        exibirMenu();
    }

    private static void exibirMenu() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- Sistema Bancário ---");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Cadastrar Conta");
            System.out.println("3. Depósito");
            System.out.println("4. Saque");
            System.out.println("5. Transferência entre Contas");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(sc.nextLine());
                switch (opcao) {
                    case 1:
                        cadastrarCliente();
                        break;
                    case 2:
                        cadastrarConta();
                        break;
                    case 3:
                        realizarDeposito();
                        break;
                    case 4:
                        realizarSaque();
                        break;
                    case 5:
                        realizarTransferencia();
                        break;
                    case 0:
                        System.out.println("Saindo do sistema. Os dados foram salvos em 'data/'.");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
            }
        }
    }

    // Funcionalidade 1: Cadastro de cliente
    private static void cadastrarCliente() {
        System.out.println("\n--- Cadastro de Cliente ---");
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("CPF (apenas números): ");
        String cpf = sc.nextLine();

        try {
            servicoCliente.cadastrarCliente(nome, cpf);
            System.out.println("Cliente " + nome + " cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    // Funcionalidade 2: Cadastro de contas
    private static void cadastrarConta() {
        System.out.println("\n--- Cadastro de Conta ---");
        System.out.print("Número da conta: ");
        String numeroConta = sc.nextLine();
        System.out.print("CPF do cliente titular: ");
        String cpfCliente = sc.nextLine();
        System.out.print("Saldo inicial: ");
        Float saldoInicial = Float.valueOf(sc.nextLine());
        System.out.print("Tipo de conta (CORRENTE/POUPANCA): ");
        String tipo = sc.nextLine().toUpperCase();

        try {
            Conta conta = servicoConta.cadastrarConta(numeroConta, cpfCliente, saldoInicial, tipo);
            System.out.println("Conta " + conta.getTipo() + " número " + conta.getNumero() + " cadastrada com sucesso para " + conta.getCliente().getNome() + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar conta: " + e.getMessage());
        }
    }

    // Funcionalidade 3: Depósito
    private static void realizarDeposito() {
        System.out.println("\n--- Depósito ---");
        System.out.print("Número da conta destino: ");
        String numeroConta = sc.nextLine();
        System.out.print("Valor do depósito: ");
        Float valor = Float.valueOf(sc.nextLine());

        try {
            servicoConta.depositar(numeroConta, valor);
            System.out.println("Depósito de R$" + valor + " realizado com sucesso na conta " + numeroConta + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao realizar depósito: " + e.getMessage());
        }
    }

    // Funcionalidade 4: Saque
    private static void realizarSaque() {
        System.out.println("\n--- Saque ---");
        System.out.print("Número da conta: ");
        String numeroConta = sc.nextLine();
        System.out.print("Valor do saque: ");
        Float valor = Float.valueOf(sc.nextLine());

        try {
            boolean sucesso = servicoConta.sacar(numeroConta, valor);
            if (sucesso) {
                System.out.println("Saque de R$" + valor + " realizado com sucesso na conta " + numeroConta + ".");
            } else {
                System.out.println("Saldo insuficiente na conta " + numeroConta + ".");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao realizar saque: " + e.getMessage());
        }
    }

    // Funcionalidade 5: Transferência entre Contas
    private static void realizarTransferencia() {
        System.out.println("\n--- Transferência ---");
        System.out.print("Número da Conta de origem: ");
        String contaOrigem = sc.nextLine();
        System.out.print("Número da conta de destino: ");
        String contaDestino = sc.nextLine();
        System.out.print("Valor da Transferência: ");
        Float valor = Float.valueOf(sc.nextLine());

        try {
            boolean sucesso = servicoConta.transferir(contaOrigem, contaDestino, valor);
            if (sucesso) {
                System.out.println("Transferência de R$" + valor + " de " + contaOrigem + " para " + contaDestino + " realizada com sucesso.");
            } else {
                System.out.println("Saldo insuficiente na conta de origem " + contaOrigem + ".");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao realizar transferência: " + e.getMessage());
        }
    }
}
