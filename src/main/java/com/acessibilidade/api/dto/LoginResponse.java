package com.acessibilidade.api.dto;

public class LoginResponse {

    private String mensagem;
    private String email;
    private String preferencia;
    private Integer tamanhoTexto;
    private String contraste;
    private String aparencia;
    private String espacamento;
    private Boolean destaque;
    private Boolean navegTeclado;
    private Boolean interfaceSimplif;

    public LoginResponse(
            String mensagem,
            String email,
            String preferencia,
            Integer tamanhoTexto,
            String contraste,
            String aparencia,
            String espacamento,
            Boolean destaque,
            Boolean navegTeclado,
            Boolean interfaceSimplif
    ) {
        this.mensagem = mensagem;
        this.email = email;
        this.preferencia = preferencia;
        this.tamanhoTexto = tamanhoTexto;
        this.contraste = contraste;
        this.aparencia = aparencia;
        this.espacamento = espacamento;
        this.destaque = destaque;
        this.navegTeclado = navegTeclado;
        this.interfaceSimplif = interfaceSimplif;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getEmail() {
        return email;
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
}