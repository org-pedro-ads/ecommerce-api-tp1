package dev.ecommerceapi.service;

import dev.ecommerceapi.entity.Pedido;
import dev.ecommerceapi.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;
    
    public List<Pedido> historicoCompras(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByDataPedidoDesc(usuarioId);
    }
    
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }
}
