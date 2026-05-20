package com.acessibilidade.api.dto;

import java.time.LocalDateTime;

public class LoginResponse {

    private String mensagem;
    private String token;
    private LocalDateTime tokenExpiraEm;
    private String email;
    private Integer tamanhoTexto;
    private Boolean contraste;
    private Boolean aparencia;
    private Float espacamento;
    private Boolean guiaLeitura;
    private Boolean navegTeclado;

    public LoginResponse(
            String mensagem,
            String token,
            LocalDateTime tokenExpiraEm,
            String email,
            Integer tamanhoTexto,
            Boolean contraste,
            Boolean aparencia,
            Float espacamento,
            Boolean guiaLeitura,
            Boolean navegTeclado
    ) {
        this.mensagem = mensagem;
        this.token = token;
        this.tokenExpiraEm = tokenExpiraEm;
        this.email = email;
        this.tamanhoTexto = tamanhoTexto;
        this.contraste = contraste;
        this.aparencia = aparencia;
        this.espacamento = espacamento;
        this.guiaLeitura = guiaLeitura;
        this.navegTeclado = navegTeclado;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getTokenExpiraEm() {
        return tokenExpiraEm;
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
}