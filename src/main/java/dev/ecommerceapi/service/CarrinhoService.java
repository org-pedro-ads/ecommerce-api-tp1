package dev.ecommerceapi.service;

import dev.ecommerceapi.dto.ItemCarrinhoDTO;
import dev.ecommerceapi.entity.*;
import dev.ecommerceapi.entity.StatusPedido;
import dev.ecommerceapi.repository.ItemPedidoRepository;
import dev.ecommerceapi.repository.PedidoRepository;
import dev.ecommerceapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarrinhoService {
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;
    
    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public Pedido obterCarrinho(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdAndStatus(usuarioId, StatusPedido.PENDENTE)
            .stream().findFirst()
            .orElseGet(() -> criarNovoCarrinho(usuarioId));
    }
    
    private Pedido criarNovoCarrinho(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + usuarioId));

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
        
        // Verifica se o produto já está no carrinho
        ItemPedido itemExistente = carrinho.getItens().stream()
            .filter(item -> item.getProduto().getId().equals(produto.getId()))
            .findFirst()
            .orElse(null);
        
        if (itemExistente != null) {
            // Atualiza a quantidade do item existente
            int novaQuantidade = dto.getQuantidade();
            itemExistente.setQuantidade(novaQuantidade);
            itemExistente.setSubTotal(produto.getPreco().multiply(new BigDecimal(novaQuantidade)));
            itemPedidoRepository.save(itemExistente);
        } else {
            // Cria um novo item
            ItemPedido novoItem = new ItemPedido();
            novoItem.setPedido(carrinho);
            novoItem.setProduto(produto);
            novoItem.setQuantidade(dto.getQuantidade());
            novoItem.setPreco(produto.getPreco());
            novoItem.setSubTotal(produto.getPreco().multiply(new BigDecimal(dto.getQuantidade())));
            
            carrinho.getItens().add(novoItem);
            itemPedidoRepository.save(novoItem);
        }
        
        // Recalcula o total do carrinho
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
