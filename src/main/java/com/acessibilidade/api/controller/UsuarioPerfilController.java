package com.acessibilidade.api.controller;

import com.acessibilidade.api.dto.AtualizarPreferenciasRequest;
import com.acessibilidade.api.dto.CriarUsuarioRequest;
import com.acessibilidade.api.model.UsuarioPerfil;
import com.acessibilidade.api.service.UsuarioPerfilService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.acessibilidade.api.model.AuditoriaPerfil;
import java.util.List;


@RestController
@RequestMapping("/api/v1/perfis")
@CrossOrigin(origins = "*")
public class UsuarioPerfilController {

    private final UsuarioPerfilService service;

    public UsuarioPerfilController(UsuarioPerfilService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioPerfil> criarUsuario(@Valid @RequestBody CriarUsuarioRequest request) {
        UsuarioPerfil usuario = service.criarUsuario(request);
        usuario.setSenha(null);

        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UsuarioPerfil> buscarPorEmail(@PathVariable String email) {
        UsuarioPerfil usuario = service.buscarPorEmail(email);
        usuario.setSenha(null);

        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{email}")
    public ResponseEntity<UsuarioPerfil> atualizarPreferencias(
            @PathVariable String email,
            @Valid @RequestBody AtualizarPreferenciasRequest request
    ) {
        UsuarioPerfil usuario = service.atualizarPreferencias(email, request);
        usuario.setSenha(null);

        return ResponseEntity.ok(usuario);
    }
    @GetMapping("/{email}/auditoria")
    public ResponseEntity<List<AuditoriaPerfil>> buscarAuditoria(@PathVariable String email) {
        List<AuditoriaPerfil> auditorias = service.buscarAuditoriaPorUsuario(email);

        return ResponseEntity.ok(auditorias);
    }    
}