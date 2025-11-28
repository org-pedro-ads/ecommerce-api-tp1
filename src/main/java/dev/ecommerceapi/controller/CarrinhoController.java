package dev.ecommerceapi.controller;

import dev.ecommerceapi.dto.ItemCarrinhoDTO;
import dev.ecommerceapi.entity.Pedido;
import dev.ecommerceapi.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrinho")
public class CarrinhoController {
    @Autowired
    private CarrinhoService carrinhoService;
    
    @GetMapping("/{usuarioId}")
    public ResponseEntity<Pedido> obterCarrinho(@PathVariable Long usuarioId) {
        Pedido carrinho = carrinhoService.obterCarrinho(usuarioId);
        return ResponseEntity.ok(carrinho);
    }
    
    @PostMapping("/{usuarioId}/itens")
    public ResponseEntity<Pedido> adicionarItem(
            @PathVariable Long usuarioId,
            @RequestBody ItemCarrinhoDTO dto) {
        Pedido carrinho = carrinhoService.adicionarItem(usuarioId, dto);
        return ResponseEntity.ok(carrinho);
    }
    
    @DeleteMapping("/itens/{itemId}")
    public ResponseEntity<Void> removerItem(@PathVariable Long itemId) {
        carrinhoService.removerItem(itemId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{usuarioId}/finalizar")
    public ResponseEntity<Pedido> finalizarCompra(@PathVariable Long usuarioId) {
        Pedido pedido = carrinhoService.finalizarCompra(usuarioId);
        return ResponseEntity.ok(pedido);
    }
}
