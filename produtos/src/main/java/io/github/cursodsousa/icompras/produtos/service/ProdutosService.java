package io.github.cursodsousa.icompras.produtos.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.cursodsousa.icompras.produtos.model.Produtos;
import io.github.cursodsousa.icompras.produtos.repository.ProdutosRepository;
import lombok.RequiredArgsConstructor;

@Service
// Cria um construtor com todo os argumentos obrigatórios
@RequiredArgsConstructor
public class ProdutosService {
    
    private final ProdutosRepository repository;

    public Produtos salvar(Produtos produtos) {
        return repository.save(produtos);
    }

    public Optional<Produtos> selecionarPorId(Long idproduto) {
        return repository.findById(idproduto);
    }
}
