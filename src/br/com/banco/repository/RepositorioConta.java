package br.com.banco.repository;

import br.com.banco.model.Conta;
import java.util.Optional;

public interface RepositorioConta extends Repositorio<Conta, String> {
    Optional<Conta> buscarPorNumero(String numero);
}
