package dev.ecommerceapi.service;

import dev.ecommerceapi.dto.LoginDTO;
import dev.ecommerceapi.dto.LoginResponseDTO;
import dev.ecommerceapi.dto.UsuarioDTO;
import dev.ecommerceapi.entity.Usuario;
import dev.ecommerceapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    public Usuario cadastrar(UsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setAdmin(dto.getAdmin());
        
        return usuarioRepository.save(usuario);
    }
    
    public LoginResponseDTO login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));
        
        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Email ou senha inválidos");
        }
        
        return new LoginResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getAdmin(),
            "Login realizado com sucesso"
        );
    }
    
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
    
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
}
