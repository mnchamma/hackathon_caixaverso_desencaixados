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

    @Column(length = 100)
    private String preferencia;

    @Column(name = "tamanho_texto")
    private Integer tamanhoTexto;

    @Column(length = 50)
    private String contraste;

    @Column(length = 50)
    private String aparencia;

    @Column(length = 50)
    private String espacamento;

    private Boolean destaque = false;

    @Column(name = "naveg_teclado")
    private Boolean navegTeclado = false;

    @Column(name = "interface_simplif")
    private Boolean interfaceSimplif = false;

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

    public String getPreferencia() {
        return preferencia;
    }

    public Integer getTamanhoTexto() {
        return tamanhoTexto;
    }

    public String getContraste() {
        return contraste;
    }

    public String getAparencia() {
        return aparencia;
    }

    public String getEspacamento() {
        return espacamento;
    }

    public Boolean getDestaque() {
        return destaque;
    }

    public Boolean getNavegTeclado() {
        return navegTeclado;
    }

    public Boolean getInterfaceSimplif() {
        return interfaceSimplif;
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

    public void setPreferencia(String preferencia) {
        this.preferencia = preferencia;
    }

    public void setTamanhoTexto(Integer tamanhoTexto) {
        this.tamanhoTexto = tamanhoTexto;
    }

    public void setContraste(String contraste) {
        this.contraste = contraste;
    }

    public void setAparencia(String aparencia) {
        this.aparencia = aparencia;
    }

    public void setEspacamento(String espacamento) {
        this.espacamento = espacamento;
    }

    public void setDestaque(Boolean destaque) {
        this.destaque = destaque;
    }

    public void setNavegTeclado(Boolean navegTeclado) {
        this.navegTeclado = navegTeclado;
    }

    public void setInterfaceSimplif(Boolean interfaceSimplif) {
        this.interfaceSimplif = interfaceSimplif;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}