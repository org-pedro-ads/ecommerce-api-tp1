package dev.ecommerceapi.infra.security;

import dev.ecommerceapi.entity.CategoriaProduto;
import dev.ecommerceapi.entity.Produto;
import dev.ecommerceapi.entity.Usuario;
import dev.ecommerceapi.repository.ProdutoRepository;
import dev.ecommerceapi.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(
            UsuarioRepository usuarioRepo,
            ProdutoRepository produtoRepo
    ) {
        return args -> {
            Usuario usuario2 = new Usuario();
            usuario2.setNome("Maria Oliveira");
            usuario2.setEmail("maria.oliveira@email.com");
            usuario2.setSenha("senha456");
            usuario2.setAdmin(false);
            usuario2.setPedidos(List.of());

            usuarioRepo.save(usuario2);

            Usuario usuario1 = new Usuario();
            usuario1.setNome("ADMIN");
            usuario1.setEmail("admin@email.com");
            usuario1.setSenha("senha123");
            usuario1.setAdmin(true);
            usuario1.setPedidos(List.of());

            usuarioRepo.save(usuario1);

            Usuario usuario3 = new Usuario();
            usuario3.setNome("Carlos Santos");
            usuario3.setEmail("carlos.santos@email.com");
            usuario3.setSenha("senha789");
            usuario3.setAdmin(false);
            usuario3.setPedidos(List.of());

            usuarioRepo.save(usuario3);

            Produto produto1 = new Produto();
            produto1.setNome("Smartphone XYZ");
            produto1.setDescricao("Smartphone com tela de 6.5 polegadas, 128GB de armazenamento");
            produto1.setPreco(new BigDecimal("1999.99"));
            produto1.setQuantidadeEstoque(50);
            produto1.setCategoria(CategoriaProduto.ELETRONICOS);
            produto1.setCaracteristicas(List.of("Tela AMOLED", "Câmera tripla", "Bateria 5000mAh"));

            produtoRepo.save(produto1);

            Produto produto2 = new Produto();
            produto2.setNome("Notebook ABC");
            produto2.setDescricao("Notebook com processador Intel Core i7, 16GB de RAM e 512GB de armazenamento");
            produto2.setPreco(new BigDecimal("2999.99"));
            produto2.setQuantidadeEstoque(30);
            produto2.setCategoria(CategoriaProduto.ELETRONICOS);
            produto2.setCaracteristicas(List.of("Processador Intel Core i7", "16GB de RAM", "512GB de armazenamento"));

            produtoRepo.save(produto2);

            Produto produto3 = new Produto();
            produto3.setNome("Smartwatch DEF");
            produto3.setDescricao("Smartwatch com tela de 1.5 polegadas, 32GB de armazenamento e 128GB de armazenamento");
            produto3.setPreco(new BigDecimal("199.99"));
            produto3.setQuantidadeEstoque(100);
            produto3.setCategoria(CategoriaProduto.ELETRONICOS);
            produto3.setCaracteristicas(List.of("Tela AMOLED", "Câmera tripla", "Bateria 5000mAh"));

            produtoRepo.save(produto3);
        };
    }
}
