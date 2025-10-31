package io.github.cursodsousa.icompras.clientes.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.cursodsousa.icompras.clientes.model.Clientes;
import io.github.cursodsousa.icompras.clientes.repository.ClientesRepository;
import lombok.RequiredArgsConstructor;

@Service
// Cria um construtor com todo os argumentos obrigatórios
@RequiredArgsConstructor
public class ClientesService {
    
    private final ClientesRepository repository;

    public Clientes salvar(Clientes clientes) {
        return repository.save(clientes);
    }

    public Optional<Clientes> selecionarPorId(Long idcliente) {
        return repository.findById(idcliente);
    }
}
