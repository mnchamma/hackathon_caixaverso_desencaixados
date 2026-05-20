package com.acessibilidade.api.dto;

import com.acessibilidade.api.model.UsuarioPerfil;
import java.time.LocalDateTime;

public class PerfilResponse {

    private Long id;
    private String email;
    private Integer tamanhoTexto;
    private Boolean contraste;
    private Boolean aparencia;
    private Float espacamento;
    private Boolean guiaLeitura;
    private Boolean navegTeclado;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public PerfilResponse(UsuarioPerfil usuario) {
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.tamanhoTexto = usuario.getTamanhoTexto();
        this.contraste = usuario.getContraste();
        this.aparencia = usuario.getAparencia();
        this.espacamento = usuario.getEspacamento();
        this.guiaLeitura = usuario.getGuiaLeitura();
        this.navegTeclado = usuario.getNavegTeclado();
        this.criadoEm = usuario.getCriadoEm();
        this.atualizadoEm = usuario.getAtualizadoEm();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
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

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}