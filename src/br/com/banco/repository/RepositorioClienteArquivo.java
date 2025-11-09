package br.com.banco.repository;

import br.com.banco.model.Cliente;
import java.util.Optional;

public class RepositorioClienteArquivo extends RepositorioArquivoAbstrato<Cliente, String> implements RepositorioCliente {

    private static final String NOME_ARQUIVO = "clientes.txt";

    public RepositorioClienteArquivo() {
        super(NOME_ARQUIVO);
    }

    @Override
    protected String serializar(Cliente entidade) {
        return entidade.toString();
    }

    @Override
    protected Optional<Cliente> deserializar(String linha) {
        try {
            return Optional.of(Cliente.fromString(linha));
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao deserializar cliente: " + linha + " - " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    protected String getId(Cliente entidade) {
        return entidade.getCpf();
    }

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return buscarPorId(cpf);
    }
}
