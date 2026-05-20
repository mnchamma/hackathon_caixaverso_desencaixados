package com.acessibilidade.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario_perfil")
public class UsuarioPerfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String senha;

    @Column(name = "tamanho_texto")
    private Integer tamanhoTexto;

    private Boolean contraste = false;

    private Boolean aparencia = false;

    private Float espacamento;

    @Column(name = "guia_leitura")
    private Boolean guiaLeitura = false;

    @Column(name = "naveg_teclado")
    private Boolean navegTeclado = false;

    @Column(name = "token", columnDefinition = "TEXT")
    private String token;

    @Column(name = "token_expira_em")
    private LocalDateTime tokenExpiraEm;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public Integer getTamanhoTexto() {
        return tamanhoTexto;
    }

    public Boolean getContraste() {
        return contraste;
    }

    public Boolean getAparencia() {
        return aparencia;
    }

    public Float getEspacamento() {
        return espacamento;
    }

    public Boolean getGuiaLeitura() {
        return guiaLeitura;
    }

    public Boolean getNavegTeclado() {
        return navegTeclado;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getTokenExpiraEm() {
        return tokenExpiraEm;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setTamanhoTexto(Integer tamanhoTexto) {
        this.tamanhoTexto = tamanhoTexto;
    }

    public void setContraste(Boolean contraste) {
        this.contraste = contraste;
    }

    public void setAparencia(Boolean aparencia) {
        this.aparencia = aparencia;
    }

    public void setEspacamento(Float espacamento) {
        this.espacamento = espacamento;
    }

    public void setGuiaLeitura(Boolean guiaLeitura) {
        this.guiaLeitura = guiaLeitura;
    }

    public void setNavegTeclado(Boolean navegTeclado) {
        this.navegTeclado = navegTeclado;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setTokenExpiraEm(LocalDateTime tokenExpiraEm) {
        this.tokenExpiraEm = tokenExpiraEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}