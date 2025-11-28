package dev.ecommerceapi.repository;

import dev.ecommerceapi.entity.Pedido;
import dev.ecommerceapi.entity.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioIdAndStatus(Long usuarioId, StatusPedido status);
    List<Pedido> findByUsuarioIdOrderByDataPedidoDesc(Long usuarioId);
}
