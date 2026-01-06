package io.github.icompras.pedidos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.icompras.pedidos.client.ClientesClient;
import io.github.icompras.pedidos.client.ProdutosClient;
import io.github.icompras.pedidos.client.ServicoBancoClient;
import io.github.icompras.pedidos.model.DadosPagamento;
import io.github.icompras.pedidos.model.ItemPedido;
import io.github.icompras.pedidos.model.Pedido;
import io.github.icompras.pedidos.model.enums.StatusPedido;
import io.github.icompras.pedidos.model.exception.PedidoNaoEncontradoException;
import io.github.icompras.pedidos.publisher.PagamentoPublisher;
import io.github.icompras.pedidos.repository.ItemPedidoRepository;
import io.github.icompras.pedidos.repository.PedidoRepository;
import io.github.icompras.pedidos.validator.PedidoValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
// Cria um construtor com todo os argumentos obrigatórios
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoValidator pedidoValidator;
    // Cliente para interagir com o serviço de banco externo
    private final ServicoBancoClient servicoBancoClient;

    private final ClientesClient clientesClient;
    private final ProdutosClient produtosClient;

    private final PagamentoPublisher pagamentoPublisher;

    /**
     * Ao finalizar a transação o pedido e seus itens serão salvos no banco de dados
     * tambem serve para não usar pedidoRepository.save(pedido) duas vezes depois de
     * pedido.setIdPagamento(pagamentoId)
     * Tudo que fizer dentro do método será persistido no final da execução do
     * método
     */
    @Transactional
    public Pedido criar(Pedido pedido) {
        // Validar o pedido antes de salvar
        validarPedido(pedido);
        percistenciaDadosPedido(pedido);
        enviarSolicitacaoPagamento(pedido);
        return pedido;
    }

    private void validarPedido(Pedido pedido) {
        pedidoValidator.validar(pedido);
    }

    private void percistenciaDadosPedido(@NonNull Pedido pedido) {
        // Criar o pedido principal
        pedidoRepository.save(pedido);
        // Valida se existem itens no pedido
        var itens = pedido.getItens();
        if (itens != null && !itens.isEmpty()) {
            // Criar e associar os itens
            itemPedidoRepository.saveAll(itens);
        }
    }

    // Solicitar pagamento ao serviço de banco e associar o ID de pagamento ao
    private void enviarSolicitacaoPagamento(Pedido pedido) {
        var pagamentoId = servicoBancoClient.solicitarPagamento(pedido);
        pedido.setIdPagamento(pagamentoId);
    }

    // Notação serve para suprimir avisos de nullabilidade
    public Optional<Pedido> selecionarPorId(Long idpedido) {
        return pedidoRepository.findById(idpedido);
    }

    public void processarCallbackPagamento(
            Long pedidoId, String idPagamento, Boolean status, String observacoes) {

        var infoPedido = pedidoRepository.findByIdpedidoAndIdPagamento(pedidoId, idPagamento);

        if (infoPedido.isEmpty()) {
            var mensagem = String.format("Pedido com ID %d e pagamento %s não encontrado", pedidoId, idPagamento);
            log.error(mensagem);
            return;
        }

        if (infoPedido.isPresent()) {
            var pedidoEncontrado = infoPedido.get();

            if (status) {
                prepararPublicarPedidoPago(pedidoEncontrado);
            } else {
                pedidoEncontrado.setStatus(StatusPedido.ERRO_PAGAMENTO);
            }
            pedidoEncontrado.setObservacoes(observacoes);
            pedidoRepository.save(pedidoEncontrado);
        }
    }

    // Lógica para preparar os dados antes de publicar o evento de pagamento
    private void prepararPublicarPedidoPago(Pedido pedidoEncontrado) {
        pedidoEncontrado.setStatus(StatusPedido.PAGAMENTO_APROVADO);
        // Carregar os dados do cliente e itens do pedido antes de publicar o evento
        carregarDadosCliente(pedidoEncontrado);
        carregarItensPedido(pedidoEncontrado);

        pagamentoPublisher.publicar(pedidoEncontrado);
    }

    @Transactional
    public void adicionarNovoPagamento(Long pedidoId, String detalhesPagamento, String metodoPagamento) {
        var infoPedido = pedidoRepository.findById(pedidoId);

        if (infoPedido.isEmpty()) {
            var mensagem = String.format("Pedido com ID %d não encontrado", pedidoId);
            throw new PedidoNaoEncontradoException(mensagem);
        }

        if (infoPedido.isPresent()) {
            var pedidoEncontrado = infoPedido.get();

            DadosPagamento dadosPagamento = new DadosPagamento();
            dadosPagamento.setDetalhesPagamento(detalhesPagamento);
            dadosPagamento.setMetodoPagamento(null);

            pedidoEncontrado.setDadosPagamento(dadosPagamento);
            pedidoEncontrado.setStatus(StatusPedido.PAGAMENTO_REALIZADO);
            pedidoEncontrado.setObservacoes("Novo pagamento realizado com sucesso, pedido em preparação.");
            // Não é necessário chamar save explicitamente devido à anotação @Transactional
            pedidoRepository.save(pedidoEncontrado);

            String pagamentoId = servicoBancoClient.solicitarPagamento(pedidoEncontrado);
            pedidoEncontrado.setIdPagamento(pagamentoId);
        }
    }

    public Optional<Pedido> carregarDadosPedido(Long idpedido) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(idpedido);
        pedidoOpt.ifPresent(this::carregarDadosCliente);
        pedidoOpt.ifPresent(this::carregarItensPedido);
        return pedidoOpt;
    }

    private void carregarDadosCliente(Pedido pedido) {
        // Lógica para carregar os dados do cliente associado ao pedido
        // Isso pode envolver chamadas a outros serviços ou repositórios
        Long idCliente = pedido.getIdCliente();
        var resposta = clientesClient.obterDados(idCliente);
        pedido.setDadosCliente(resposta.getBody());
    }

    private void carregarItensPedido(Pedido pedido) {
        // Lógica para carregar os itens do pedido
        // Isso pode envolver chamadas a outros serviços ou repositórios
        List<ItemPedido> itensPedido = itemPedidoRepository.findByPedido(pedido);
        pedido.setItens(itensPedido);
        pedido.getItens().forEach(this::carregarDadosProduto);
    }

    @SuppressWarnings("null")
    private void carregarDadosProduto(ItemPedido itemPedido) {
        Long idProduto = itemPedido.getIdProduto();
        var resposta = produtosClient.obterDados(idProduto);
        // Metodo para setar as propriedades do produto no itemPedido
        itemPedido.setNomeProduto(resposta.getBody().nome());
    }
}
