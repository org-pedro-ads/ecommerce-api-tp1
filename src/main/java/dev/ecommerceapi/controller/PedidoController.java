package dev.ecommerceapi.controller;

import dev.ecommerceapi.entity.Pedido;
import dev.ecommerceapi.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;
    
    @GetMapping("/usuario/{usuarioId}/historico")
    public ResponseEntity<List<Pedido>> historicoCompras(@PathVariable Long usuarioId) {
        List<Pedido> historico = pedidoService.historicoCompras(usuarioId);
        return ResponseEntity.ok(historico);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }
}
