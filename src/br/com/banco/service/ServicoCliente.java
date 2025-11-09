package br.com.banco.service;

import br.com.banco.model.Cliente;
import br.com.banco.repository.RepositorioCliente;

import java.util.Optional;

public class ServicoCliente {

    private final RepositorioCliente repositorioCliente;

    public ServicoCliente(RepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    public Cliente cadastrarCliente(String nome, String cpf) {
        if (repositorioCliente.buscarPorCpf(cpf).isPresent()) {
            throw new IllegalArgumentException("Cliente com CPF " + cpf + " já cadastrado.");
        }

        Cliente novoCliente = new Cliente(nome, cpf);
        repositorioCliente.salvar(novoCliente);
        return novoCliente;
    }

    public boolean clienteExiste(String cpf) {
        return repositorioCliente.buscarPorCpf(cpf).isPresent();
    }

    public Optional<Cliente> buscarClientePorCpf(String cpf) {
        return repositorioCliente.buscarPorCpf(cpf);
    }
}
