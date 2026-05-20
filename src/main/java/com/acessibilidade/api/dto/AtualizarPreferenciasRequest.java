package com.acessibilidade.api.dto;

import jakarta.validation.constraints.NotNull;

public class AtualizarPreferenciasRequest {

    private String preferencia;

    @NotNull
    private Integer tamanhoTexto;

    private String contraste;

    private String aparencia;

    private String espacamento;

    private Boolean destaque;

    private Boolean navegTeclado;

    private Boolean interfaceSimplif;

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
}