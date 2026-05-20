 package com.acessibilidade.api.model;

import com.acessibilidade.api.dto.PerfilResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ModelTest {

    @Test
    void usuarioPerfilGettersSetters() {
        UsuarioPerfil u = new UsuarioPerfil();
        u.setId(1L);
        u.setEmail("e");
        u.setSenha("s");
        u.setTamanhoTexto(10);
        u.setContraste(true);
        u.setAparencia(true);
        u.setEspacamento(1.5f);
        u.setGuiaLeitura(true);
        u.setNavegTeclado(true);
        u.setToken("t");
        LocalDateTime now = LocalDateTime.now();
        u.setTokenExpiraEm(now);
        u.setCriadoEm(now);
        u.setAtualizadoEm(now);
        u.prePersist();
        u.preUpdate();

        assertThat(u.getId()).isEqualTo(1L);
        assertThat(u.getEmail()).isEqualTo("e");
        assertThat(u.getSenha()).isEqualTo("s");
        assertThat(u.getTamanhoTexto()).isEqualTo(10);
        assertThat(u.getContraste()).isTrue();
        assertThat(u.getAparencia()).isTrue();
        assertThat(u.getEspacamento()).isEqualTo(1.5f);
        assertThat(u.getGuiaLeitura()).isTrue();
        assertThat(u.getNavegTeclado()).isTrue();
        assertThat(u.getToken()).isEqualTo("t");
        assertThat(u.getTokenExpiraEm()).isNotNull();
        assertThat(u.getCriadoEm()).isNotNull();
        assertThat(u.getAtualizadoEm()).isNotNull();
    }

    @Test
    void auditoriaPerfilGettersSetters() {
        AuditoriaPerfil a = new AuditoriaPerfil();
        a.setId(1L);
        a.setUsuarioEmail("e");
        a.setCampoAlterado("c");
        a.setValorAnterior("va");
        a.setValorNovo("vn");
        a.setDataAlteracao(LocalDateTime.now());
        a.prePersist();

        assertThat(a.getId()).isEqualTo(1L);
        assertThat(a.getUsuarioEmail()).isEqualTo("e");
        assertThat(a.getCampoAlterado()).isEqualTo("c");
        assertThat(a.getValorAnterior()).isEqualTo("va");
        assertThat(a.getValorNovo()).isEqualTo("vn");
        assertThat(a.getDataAlteracao()).isNotNull();
    }

    @Test
    void perfilResponseMapeiaUsuario() {
        UsuarioPerfil u = new UsuarioPerfil();
        u.setId(99L);
        u.setEmail("p@caixa.gov.br");
        u.setTamanhoTexto(18);
        u.setContraste(true);
        u.setAparencia(false);
        u.setEspacamento(1.2f);
        u.setGuiaLeitura(true);
        u.setNavegTeclado(true);
        LocalDateTime now = LocalDateTime.now();
        u.setCriadoEm(now);
        u.setAtualizadoEm(now);

        PerfilResponse r = new PerfilResponse(u);
        assertThat(r.getId()).isEqualTo(99L);
        assertThat(r.getEmail()).isEqualTo("p@caixa.gov.br");
        assertThat(r.getTamanhoTexto()).isEqualTo(18);
        assertThat(r.getContraste()).isTrue();
        assertThat(r.getAparencia()).isFalse();
        assertThat(r.getEspacamento()).isEqualTo(1.2f);
        assertThat(r.getGuiaLeitura()).isTrue();
        assertThat(r.getNavegTeclado()).isTrue();
        assertThat(r.getCriadoEm()).isEqualTo(now);
        assertThat(r.getAtualizadoEm()).isEqualTo(now);
    }
}

