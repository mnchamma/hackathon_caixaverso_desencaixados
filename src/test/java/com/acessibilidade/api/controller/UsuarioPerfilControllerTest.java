package com.acessibilidade.api.controller;

import com.acessibilidade.api.dto.AtualizarPreferenciasRequest;
import com.acessibilidade.api.dto.BuscarPerfilRequest;
import com.acessibilidade.api.dto.CriarUsuarioRequest;
import com.acessibilidade.api.model.AuditoriaPerfil;
import com.acessibilidade.api.model.UsuarioPerfil;
import com.acessibilidade.api.service.UsuarioPerfilService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsuarioPerfilControllerTest {

    private UsuarioPerfilService service;
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @BeforeEach
    void setUp() {
        service = Mockito.mock(UsuarioPerfilService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new UsuarioPerfilController(service)).build();
    }

    private UsuarioPerfil exemplo() {
        UsuarioPerfil u = new UsuarioPerfil();
        u.setId(1L);
        u.setEmail("a@caixa.gov.br");
        u.setTamanhoTexto(14);
        u.setContraste(true);
        u.setAparencia(false);
        u.setEspacamento(1.0f);
        u.setGuiaLeitura(true);
        u.setNavegTeclado(false);
        return u;
    }

    @Test
    void criarUsuarioOk() throws Exception {
        CriarUsuarioRequest req = new CriarUsuarioRequest();
        req.setEmail("a@caixa.gov.br");
        req.setSenha("senha");
        req.setTamanhoTexto(14);
        req.setContraste(true);
        req.setAparencia(false);
        req.setEspacamento(1.0f);
        req.setGuiaLeitura(true);
        req.setNavegTeclado(false);

        when(service.criarUsuario(any())).thenReturn(exemplo());

        mockMvc.perform(post("/api/v1/perfis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("a@caixa.gov.br"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void buscarPerfilOk() throws Exception {
        BuscarPerfilRequest req = new BuscarPerfilRequest();
        req.setEmail("a@caixa.gov.br");
        when(service.buscarPorEmail(any(), anyString())).thenReturn(exemplo());

        mockMvc.perform(post("/api/v1/perfis/consultar")
                        .header("Authorization", "Bearer tok")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("a@caixa.gov.br"));
    }

    @Test
    void atualizarOk() throws Exception {
        AtualizarPreferenciasRequest req = new AtualizarPreferenciasRequest();
        req.setEmail("a@caixa.gov.br");
        req.setTamanhoTexto(20);
        req.setContraste(true);
        req.setAparencia(true);
        req.setEspacamento(2.0f);
        req.setGuiaLeitura(true);
        req.setNavegTeclado(true);
        when(service.atualizarPreferencias(any(), anyString())).thenReturn(exemplo());

        mockMvc.perform(put("/api/v1/perfis")
                        .header("Authorization", "Bearer tok")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void auditoriaOk() throws Exception {
        BuscarPerfilRequest req = new BuscarPerfilRequest();
        req.setEmail("a@caixa.gov.br");
        when(service.buscarAuditoriaPorUsuario(any(), anyString()))
                .thenReturn(List.of(new AuditoriaPerfil()));

        mockMvc.perform(post("/api/v1/perfis/auditoria")
                        .header("Authorization", "Bearer tok")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}

