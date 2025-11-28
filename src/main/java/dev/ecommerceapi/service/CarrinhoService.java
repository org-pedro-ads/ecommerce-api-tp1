package dev.ecommerceapi.service;

import dev.ecommerceapi.dto.ItemCarrinhoDTO;
import dev.ecommerceapi.entity.*;
import dev.ecommerceapi.entity.StatusPedido;
import dev.ecommerceapi.repository.ItemPedidoRepository;
import dev.ecommerceapi.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CarrinhoService {
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;
    
    @Autowired
    private ProdutoService produtoService;
    
    public Pedido obterCarrinho(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdAndStatus(usuarioId, StatusPedido.PENDENTE)
            .stream().findFirst()
            .orElseGet(() -> criarNovoCarrinho(usuarioId));
    }
    
    private Pedido criarNovoCarrinho(Long usuarioId) {
        Pedido pedido = new Pedido();
        pedido.setUsuario(new Usuario());
        pedido.getUsuario().setId(usuarioId);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setValorTotal(BigDecimal.ZERO);
        return pedidoRepository.save(pedido);
    }
    
    public Pedido adicionarItem(Long usuarioId, ItemCarrinhoDTO dto) {
        Pedido carrinho = obterCarrinho(usuarioId);
        Produto produto = produtoService.buscarPorId(dto.getProdutoId());
        
        if (produto.getQuantidadeEstoque() < dto.getQuantidade()) {
            throw new RuntimeException("Estoque insuficiente");
        }
        
        ItemPedido item = new ItemPedido();
        item.setPedido(carrinho);
        item.setProduto(produto);
        item.setQuantidade(dto.getQuantidade());
        item.setPreco(produto.getPreco());
        item.setSubTotal(produto.getPreco().multiply(new BigDecimal(dto.getQuantidade())));
        
        itemPedidoRepository.save(item);
        recalcularTotal(carrinho);
        
        return carrinho;
    }
    
    public void removerItem(Long itemId) {
        ItemPedido item = itemPedidoRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Item não encontrado"));
        
        Pedido carrinho = item.getPedido();
        itemPedidoRepository.delete(item);
        recalcularTotal(carrinho);
    }
    
    public Pedido finalizarCompra(Long usuarioId) {
        Pedido carrinho = obterCarrinho(usuarioId);
        
        if (carrinho.getItens().isEmpty()) {
            throw new RuntimeException("Carrinho vazio");
        }
        
        // Atualiza estoque
        for (ItemPedido item : carrinho.getItens()) {
            produtoService.atualizarEstoque(item.getProduto().getId(), item.getQuantidade());
        }
        
        carrinho.setStatus(StatusPedido.PAGO);
        carrinho.setDataPedido(LocalDateTime.now());
        
        return pedidoRepository.save(carrinho);
    }
    
    private void recalcularTotal(Pedido pedido) {
        BigDecimal total = pedido.getItens().stream()
            .map(ItemPedido::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        pedido.setValorTotal(total);
        pedidoRepository.save(pedido);
    }
}
