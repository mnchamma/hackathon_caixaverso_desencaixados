package com.acessibilidade.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AtualizarPreferenciasRequest {

    @Email
    @NotBlank
    private String email;

    @NotNull
    private Integer tamanhoTexto;

    @NotNull
    private Boolean contraste;

    @NotNull
    private Boolean aparencia;

    @NotNull
    private Float espacamento;

    @NotNull
    private Boolean guiaLeitura;

    @NotNull
    private Boolean navegTeclado;

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

    public void setEmail(String email) {
        this.email = email;
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
}