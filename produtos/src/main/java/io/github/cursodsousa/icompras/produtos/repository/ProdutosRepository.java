package io.github.cursodsousa.icompras.produtos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.cursodsousa.icompras.produtos.model.Produtos;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutosRepository extends JpaRepository<Produtos, Long> {

}
