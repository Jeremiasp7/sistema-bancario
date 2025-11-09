package br.com.banco.repository;

import br.com.banco.model.Cliente;
import java.util.Optional;

public interface RepositorioCliente extends Repositorio<Cliente, String> {
    Optional<Cliente> buscarPorCpf(String cpf);
}
