package com.acessibilidade.api.service;

import com.acessibilidade.api.dto.AtualizarPreferenciasRequest;
import com.acessibilidade.api.dto.BuscarPerfilRequest;
import com.acessibilidade.api.dto.CriarUsuarioRequest;
import com.acessibilidade.api.exception.ApiException;
import com.acessibilidade.api.model.AuditoriaPerfil;
import com.acessibilidade.api.model.UsuarioPerfil;
import com.acessibilidade.api.repository.UsuarioPerfilRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioPerfilService {

    private final UsuarioPerfilRepository usuarioRepository;
    private final AuditoriaService auditoriaService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioPerfilService(
            UsuarioPerfilRepository usuarioRepository,
            AuditoriaService auditoriaService,
            BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UsuarioPerfil criarUsuario(CriarUsuarioRequest request) {
        validarEmailCaixa(request.getEmail());

        String emailNormalizado = request.getEmail().toLowerCase();

        if (usuarioRepository.existsByEmail(emailNormalizado)) {
            throw new ApiException(HttpStatus.CONFLICT, "E-mail ja cadastrado.");
        }

        UsuarioPerfil usuario = new UsuarioPerfil();

        usuario.setEmail(emailNormalizado);
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setTamanhoTexto(request.getTamanhoTexto());
        usuario.setContraste(Boolean.TRUE.equals(request.getContraste()));
        usuario.setAparencia(Boolean.TRUE.equals(request.getAparencia()));
        usuario.setEspacamento(request.getEspacamento());
        usuario.setGuiaLeitura(Boolean.TRUE.equals(request.getGuiaLeitura()));
        usuario.setNavegTeclado(Boolean.TRUE.equals(request.getNavegTeclado()));

        UsuarioPerfil usuarioSalvo = usuarioRepository.save(usuario);

        auditoriaService.registrar(
                usuarioSalvo.getEmail(),
                "CADASTRO",
                null,
                "Perfil de acessibilidade criado"
        );

        return usuarioSalvo;
    }

    public UsuarioPerfil buscarPorEmail(BuscarPerfilRequest request, String authorizationHeader) {
        validarEmailCaixa(request.getEmail());

        String emailNormalizado = request.getEmail().toLowerCase();

        validarTokenDaRequisicao(emailNormalizado, authorizationHeader);

        return usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuario nao encontrado."));
    }

    public UsuarioPerfil atualizarPreferencias(
            AtualizarPreferenciasRequest request,
            String authorizationHeader
    ) {
        validarEmailCaixa(request.getEmail());

        String emailNormalizado = request.getEmail().toLowerCase();

        validarTokenDaRequisicao(emailNormalizado, authorizationHeader);

        UsuarioPerfil usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuario nao encontrado."));

        auditoriaService.registrar(emailNormalizado, "tamanho_texto", String.valueOf(usuario.getTamanhoTexto()), String.valueOf(request.getTamanhoTexto()));
        auditoriaService.registrar(emailNormalizado, "contraste", String.valueOf(usuario.getContraste()), String.valueOf(request.getContraste()));
        auditoriaService.registrar(emailNormalizado, "aparencia", String.valueOf(usuario.getAparencia()), String.valueOf(request.getAparencia()));
        auditoriaService.registrar(emailNormalizado, "espacamento", String.valueOf(usuario.getEspacamento()), String.valueOf(request.getEspacamento()));
        auditoriaService.registrar(emailNormalizado, "guia_leitura", String.valueOf(usuario.getGuiaLeitura()), String.valueOf(request.getGuiaLeitura()));
        auditoriaService.registrar(emailNormalizado, "naveg_teclado", String.valueOf(usuario.getNavegTeclado()), String.valueOf(request.getNavegTeclado()));

        usuario.setTamanhoTexto(request.getTamanhoTexto());
        usuario.setContraste(Boolean.TRUE.equals(request.getContraste()));
        usuario.setAparencia(Boolean.TRUE.equals(request.getAparencia()));
        usuario.setEspacamento(request.getEspacamento());
        usuario.setGuiaLeitura(Boolean.TRUE.equals(request.getGuiaLeitura()));
        usuario.setNavegTeclado(Boolean.TRUE.equals(request.getNavegTeclado()));

        return usuarioRepository.save(usuario);
    }

    public List<AuditoriaPerfil> buscarAuditoriaPorUsuario(
            BuscarPerfilRequest request,
            String authorizationHeader
    ) {
        validarEmailCaixa(request.getEmail());

        String emailNormalizado = request.getEmail().toLowerCase();

        validarTokenDaRequisicao(emailNormalizado, authorizationHeader);

        return auditoriaService.buscarPorUsuario(emailNormalizado);
    }

    private void validarTokenDaRequisicao(String email, String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Token nao informado.");
        }

        String token = authorizationHeader.replace("Bearer ", "");

        if (!usuarioRepository.existsByEmail(email)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Usuario nao encontrado.");
        }

        if (!jwtService.tokenValido(token, email)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Token invalido ou expirado.");
        }
    }

    private void validarEmailCaixa(String email) {
        if (email == null || !email.toLowerCase().endsWith("@caixa.gov.br")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Apenas e-mails @caixa.gov.br sao permitidos.");
        }
    }
}
