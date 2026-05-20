package com.acessibilidade.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_perfil")
public class AuditoriaPerfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="usuario_email", nullable = false, length = 150)
    private String usuarioEmail;

    @Column(name="campo_alterado", length = 100)
    private String campoAlterado;

    @Column(name="valor_anterior", columnDefinition = "TEXT")
    private String valorAnterior;

    @Column(name="valor_novo", columnDefinition = "TEXT")
    private String valorNovo;

    @Column(name="data_alteracao")
    private LocalDateTime dataAlteracao;

    @PrePersist
    public void prePersist() {
        this.dataAlteracao = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public String getCampoAlterado() {
        return campoAlterado;
    }

    public String getValorAnterior() {
        return valorAnterior;
    }

    public String getValorNovo() {
        return valorNovo;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public void setCampoAlterado(String campoAlterado) {
        this.campoAlterado = campoAlterado;
    }

    public void setValorAnterior(String valorAnterior) {
        this.valorAnterior = valorAnterior;
    }

    public void setValorNovo(String valorNovo) {
        this.valorNovo = valorNovo;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }
}