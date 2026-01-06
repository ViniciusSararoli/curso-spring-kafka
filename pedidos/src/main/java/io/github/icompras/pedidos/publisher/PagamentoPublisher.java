package io.github.icompras.pedidos.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.icompras.pedidos.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PagamentoPublisher {
    private final DetalhePedidoMapper detalhePedidoMapper;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${icompraspedidos.config.kafka.topics.pagamento}")
    private String pagamentoTopic;

    public void publicar(Pedido pedido) {
        // Lógica para publicar o evento de pagamento
        log.info("Pagamento publicado para o pedido ID: " + pedido.getIdpedido());
        // Necessário para tratar possíveis exceções ao publicar
        try {
            var detalhePedido = detalhePedidoMapper.map(pedido);
            // Método para converter objeto em String JSON
            String mensagem = objectMapper.writeValueAsString(detalhePedido);
            // Fá o envio da mensagem para o tópico Kafka
            kafkaTemplate.send(pagamentoTopic, "dados", mensagem);
            log.info("Mensagem de pagamento enviada para o tópico: " + pagamentoTopic);
        } catch (JsonProcessingException e) {
            log.error("Erro ao publicar pagamento para o pedido ID: " + pedido.getIdpedido(), e);
        } catch (RuntimeException e) {
            log.error("Erro inesperado ao publicar no tópico " + pagamentoTopic + " para o pedido ID: "
                    + pedido.getIdpedido(), e);
        }
    }
}
