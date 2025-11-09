package br.com.banco.repository;

import br.com.banco.model.Cliente;
import br.com.banco.model.Conta;
import br.com.banco.model.ContaCorrente;
import br.com.banco.model.ContaPoupanca;
import java.util.Optional;

public class RepositorioContaArquivo extends RepositorioArquivoAbstrato<Conta, String> implements RepositorioConta {

    private static final String NOME_ARQUIVO = "contas.txt";
    private final RepositorioCliente repositorioCliente;

    public RepositorioContaArquivo(RepositorioCliente repositorioCliente) {
        super(NOME_ARQUIVO);
        this.repositorioCliente = repositorioCliente;
    }

    @Override
    protected String serializar(Conta entidade) {
        return entidade.toString();
    }

    @Override
    protected Optional<Conta> deserializar(String linha) {
        try {
            String[] parts = linha.split(";");
            if (parts.length != 4) {
                throw new IllegalArgumentException("Linha de conta inválida.");
            }
            String tipo = parts[0];
            String cpfCliente = parts[2];

            Optional<Cliente> clienteOpt = repositorioCliente.buscarPorCpf(cpfCliente);
            if (clienteOpt.isEmpty()) {
                System.err.println("Cliente com CPF " + cpfCliente + " não encontrado para a conta: " + linha);
                return Optional.empty();
            }
            Cliente cliente = clienteOpt.get();

            if (ContaCorrente.TIPO.equals(tipo)) {
                return Optional.of(ContaCorrente.fromString(linha, cliente));
            } else if (ContaPoupanca.TIPO.equals(tipo)) {
                return Optional.of(ContaPoupanca.fromString(linha, cliente));
            } else {
                System.err.println("Tipo de conta desconhecido: " + tipo);
                return Optional.empty();
            }
        } catch (Exception e) {
            System.err.println("Erro ao deserializar conta: " + linha + " - " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    protected String getId(Conta entidade) {
        return entidade.getNumero();
    }

    @Override
    public Optional<Conta> buscarPorNumero(String numero) {
        return buscarPorId(numero);
    }
}
