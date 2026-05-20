package com.acessibilidade.api.controller;

import com.acessibilidade.api.dto.AtualizarPreferenciasRequest;
import com.acessibilidade.api.dto.BuscarPerfilRequest;
import com.acessibilidade.api.dto.CriarUsuarioRequest;
import com.acessibilidade.api.dto.PerfilResponse;
import com.acessibilidade.api.model.AuditoriaPerfil;
import com.acessibilidade.api.model.UsuarioPerfil;
import com.acessibilidade.api.service.UsuarioPerfilService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/perfis")
public class UsuarioPerfilController {

    private final UsuarioPerfilService service;

    public UsuarioPerfilController(UsuarioPerfilService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PerfilResponse> criarUsuario(@Valid @RequestBody CriarUsuarioRequest request) {
        UsuarioPerfil usuario = service.criarUsuario(request);

        return ResponseEntity.ok(new PerfilResponse(usuario));
    }

    @PostMapping("/consultar")
    public ResponseEntity<PerfilResponse> buscarPerfil(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody BuscarPerfilRequest request
    ) {
        UsuarioPerfil usuario = service.buscarPorEmail(request, authorizationHeader);

        return ResponseEntity.ok(new PerfilResponse(usuario));
    }

    @PutMapping
    public ResponseEntity<PerfilResponse> atualizarPreferencias(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody AtualizarPreferenciasRequest request
    ) {
        UsuarioPerfil usuario = service.atualizarPreferencias(request, authorizationHeader);

        return ResponseEntity.ok(new PerfilResponse(usuario));
    }

    @PostMapping("/auditoria")
    public ResponseEntity<List<AuditoriaPerfil>> buscarAuditoria(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody BuscarPerfilRequest request
    ) {
        List<AuditoriaPerfil> auditorias = service.buscarAuditoriaPorUsuario(request, authorizationHeader);

        return ResponseEntity.ok(auditorias);
    }
}