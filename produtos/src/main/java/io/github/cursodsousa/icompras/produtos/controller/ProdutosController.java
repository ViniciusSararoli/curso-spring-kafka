package io.github.cursodsousa.icompras.produtos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.cursodsousa.icompras.produtos.model.Produtos;
import io.github.cursodsousa.icompras.produtos.service.ProdutosService;
import lombok.RequiredArgsConstructor;

@RestController
// Para fazer injeção de dependencia
@RequiredArgsConstructor
// Endpoit a ser consumido
@RequestMapping("produtos")
public class ProdutosController {
    private final ProdutosService service;

    @PostMapping
    public ResponseEntity<Produtos> salvar(@RequestBody Produtos produtos) {
        service.salvar(produtos);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("{idproduto}")
    public ResponseEntity<Produtos> obterDados(@PathVariable("idproduto") Long idproduto) {
        return service
                .selecionarPorId(idproduto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
