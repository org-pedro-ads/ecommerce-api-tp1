package dev.ecommerceapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private Boolean admin;
    private String mensagem;

    public LoginResponseDTO(Long id, String nome, String email, Boolean admin,  String mensagem) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.admin = admin;
        this.mensagem = mensagem;
    }
}