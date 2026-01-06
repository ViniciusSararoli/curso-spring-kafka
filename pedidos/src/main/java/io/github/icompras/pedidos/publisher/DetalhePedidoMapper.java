package io.github.icompras.pedidos.publisher;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import io.github.icompras.pedidos.model.Pedido;
import io.github.icompras.pedidos.publisher.representarion.DetalhePedidoRepresentation;

@Mapper(componentModel = "spring", uses = { DetalheItemPedidoMapper.class })
public interface DetalhePedidoMapper {
    // source = origem = Pedido, target = destino = DetalhePedidoRepresentation
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(source = "idpedido", target = "pedidoId")
    @Mapping(source = "idCliente", target = "clienteId")
    @Mapping(expression = "java(pedido.getDia() != null ? java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss\").format(pedido.getDia()) : null)", target = "dia")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "total", target = "total")
    // Equivalente aos dados do ClientesRepresentation.java
    @Mapping(source = "dadosCliente.cpfCnpj", target = "cpfCnpj")
    @Mapping(source = "dadosCliente.nome", target = "nomeCliente")
    @Mapping(source = "dadosCliente.email", target = "email")
    @Mapping(source = "dadosCliente.telefone", target = "telefone")
    
    @Mapping(target = "itens", source = "itens")

    DetalhePedidoRepresentation map(Pedido pedido);
}
