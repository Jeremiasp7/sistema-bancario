package br.com.banco.repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class RepositorioArquivoAbstrato<T, ID> implements Repositorio<T, ID> {

    private final String nomeArquivo;
    private final Path caminhoArquivo;

    public RepositorioArquivoAbstrato(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        Path dataDir = Paths.get("data");
        try {
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar diretório de dados: " + dataDir, e);
        }
        this.caminhoArquivo = dataDir.resolve(nomeArquivo);
    }

    protected abstract String serializar(T entidade);
    protected abstract Optional<T> deserializar(String linha);
    protected abstract ID getId(T entidade);

    @Override
    public void salvar(T entidade) {
        List<T> entidades = buscarTodos();
        ID id = getId(entidade);

        entidades = entidades.stream()
                .filter(e -> !getId(e).equals(id))
                .collect(Collectors.toList());

        entidades.add(entidade);

        escreverTodos(entidades);
    }

    @Override
    public Optional<T> buscarPorId(ID id) {
        return buscarTodos().stream()
                .filter(entidade -> getId(entidade).equals(id))
                .findFirst();
    }

    @Override
    public List<T> buscarTodos() {
        List<T> entidades = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(caminhoArquivo)) {
            String linha;
            while ((linha = br.readLine()) != null) {
                deserializar(linha).ifPresent(entidades::add);
            }
        } catch (FileNotFoundException e) {
            
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + nomeArquivo, e);
        }
        return entidades;
    }

    @Override
    public void remover(ID id) {
        List<T> entidades = buscarTodos().stream()
                .filter(entidade -> !getId(entidade).equals(id))
                .collect(Collectors.toList());

        escreverTodos(entidades);
    }

    private void escreverTodos(List<T> entidades) {
        try (BufferedWriter bw = Files.newBufferedWriter(caminhoArquivo)) {
            for (T entidade : entidades) {
                bw.write(serializar(entidade));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever no arquivo: " + nomeArquivo, e);
        }
    }
}
