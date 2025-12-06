package dev.ecommerceapi.dto;

import dev.ecommerceapi.entity.CategoriaProduto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProdutoDTO {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer quantidadeEstoque;
    private CategoriaProduto categoria;
    private List<String> caracteristicas;
}
