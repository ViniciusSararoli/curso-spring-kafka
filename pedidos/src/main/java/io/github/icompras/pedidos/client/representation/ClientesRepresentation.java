package io.github.icompras.pedidos.client.representation;

public record ClientesRepresentation(
        Long idcliente, String nome, String email, String telefone, String cpfCnpj) {

}
