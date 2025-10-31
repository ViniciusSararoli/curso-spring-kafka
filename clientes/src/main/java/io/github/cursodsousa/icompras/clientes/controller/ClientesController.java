package io.github.cursodsousa.icompras.clientes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.cursodsousa.icompras.clientes.model.Clientes;
import io.github.cursodsousa.icompras.clientes.service.ClientesService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("clientes")
public class ClientesController {
    private final ClientesService service;

    @PostMapping
    public ResponseEntity<Clientes> salvar(@RequestBody Clientes clientes) {
        service.salvar(clientes);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("{idcliente}")
    public ResponseEntity<Clientes> obterDados(@PathVariable("idcliente") Long idcliente) {
        return service
                .selecionarPorId(idcliente)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
