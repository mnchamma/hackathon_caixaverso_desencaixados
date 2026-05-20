package com.acessibilidade.api.service;

import com.acessibilidade.api.model.AuditoriaPerfil;
import com.acessibilidade.api.repository.AuditoriaPerfilRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditoriaServiceTest {

    @Mock
    private AuditoriaPerfilRepository repository;

    @InjectMocks
    private AuditoriaService service;

    @Test
    void naoRegistraQuandoAmbosNull() {
        service.registrar("a@caixa.gov.br", "campo", null, null);
        verifyNoInteractions(repository);
    }

    @Test
    void naoRegistraQuandoValoresIguais() {
        service.registrar("a@caixa.gov.br", "campo", "x", "x");
        verifyNoInteractions(repository);
    }

    @Test
    void registraQuandoValoresDiferentes() {
        ArgumentCaptor<AuditoriaPerfil> captor = ArgumentCaptor.forClass(AuditoriaPerfil.class);

        service.registrar("USER@CAIXA.GOV.BR", "campo", "antigo", "novo");

        verify(repository).save(captor.capture());
        AuditoriaPerfil salvo = captor.getValue();
        assertThat(salvo.getUsuarioEmail()).isEqualTo("user@caixa.gov.br");
        assertThat(salvo.getCampoAlterado()).isEqualTo("campo");
        assertThat(salvo.getValorAnterior()).isEqualTo("antigo");
        assertThat(salvo.getValorNovo()).isEqualTo("novo");
    }

    @Test
    void registraQuandoAnteriorNullENovoPresente() {
        service.registrar("u@caixa.gov.br", "c", null, "n");
        verify(repository).save(any(AuditoriaPerfil.class));
    }

    @Test
    void buscarPorUsuarioNormalizaEmail() {
        when(repository.findByUsuarioEmailOrderByDataAlteracaoDesc("u@caixa.gov.br"))
                .thenReturn(List.of(new AuditoriaPerfil()));

        List<AuditoriaPerfil> result = service.buscarPorUsuario("U@CAIXA.GOV.BR");

        assertThat(result).hasSize(1);
        verify(repository).findByUsuarioEmailOrderByDataAlteracaoDesc("u@caixa.gov.br");
    }
}

