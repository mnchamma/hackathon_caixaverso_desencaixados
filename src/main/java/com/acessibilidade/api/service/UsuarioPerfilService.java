package com.acessibilidade.api.service;

import com.acessibilidade.api.dto.AtualizarPreferenciasRequest;
import com.acessibilidade.api.dto.BuscarPerfilRequest;
import com.acessibilidade.api.dto.CriarUsuarioRequest;
import com.acessibilidade.api.dto.LoginRequest;
import com.acessibilidade.api.dto.LoginResponse;
import com.acessibilidade.api.model.AuditoriaPerfil;
import com.acessibilidade.api.model.UsuarioPerfil;
import com.acessibilidade.api.repository.AuditoriaPerfilRepository;
import com.acessibilidade.api.repository.UsuarioPerfilRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioPerfilService {

    private final UsuarioPerfilRepository usuarioRepository;
    private final AuditoriaPerfilRepository auditoriaRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioPerfilService(
            UsuarioPerfilRepository usuarioRepository,
            AuditoriaPerfilRepository auditoriaRepository,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.auditoriaRepository = auditoriaRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UsuarioPerfil criarUsuario(CriarUsuarioRequest request) {
        validarEmailCaixa(request.getEmail());

        String emailNormalizado = request.getEmail().toLowerCase();

        if (usuarioRepository.existsByEmail(emailNormalizado)) {
            throw new RuntimeException("E-mail já cadastrado.");
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

        auditar(
                usuarioSalvo.getEmail(),
                "CADASTRO",
                null,
                "Perfil de acessibilidade criado"
        );

        return usuarioSalvo;
    }

    public LoginResponse login(LoginRequest request) {
        validarEmailCaixa(request.getEmail());

        String emailNormalizado = request.getEmail().toLowerCase();

        UsuarioPerfil usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new RuntimeException("E-mail ou senha inválidos."));

        boolean senhaValida = passwordEncoder.matches(
                request.getSenha(),
                usuario.getSenha()
        );

        if (!senhaValida) {
            throw new RuntimeException("E-mail ou senha inválidos.");
        }

        String token = jwtService.gerarToken(usuario.getEmail());
        LocalDateTime tokenExpiraEm = jwtService.calcularExpiracao();

        usuario.setToken(token);
        usuario.setTokenExpiraEm(tokenExpiraEm);

        usuarioRepository.save(usuario);

        auditar(
                usuario.getEmail(),
                "LOGIN",
                null,
                "Novo token JWT gerado"
        );

        return new LoginResponse(
                "Login realizado com sucesso.",
                token,
                tokenExpiraEm,
                usuario.getEmail(),
                usuario.getTamanhoTexto(),
                usuario.getContraste(),
                usuario.getAparencia(),
                usuario.getEspacamento(),
                usuario.getGuiaLeitura(),
                usuario.getNavegTeclado()
        );
    }

    public UsuarioPerfil buscarPorEmail(BuscarPerfilRequest request, String authorizationHeader) {
        validarEmailCaixa(request.getEmail());

        String emailNormalizado = request.getEmail().toLowerCase();

        validarTokenDaRequisicao(emailNormalizado, authorizationHeader);

        return usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }

    public UsuarioPerfil atualizarPreferencias(
            AtualizarPreferenciasRequest request,
            String authorizationHeader
    ) {
        validarEmailCaixa(request.getEmail());

        String emailNormalizado = request.getEmail().toLowerCase();

        validarTokenDaRequisicao(emailNormalizado, authorizationHeader);

        UsuarioPerfil usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        auditar(emailNormalizado, "tamanho_texto", String.valueOf(usuario.getTamanhoTexto()), String.valueOf(request.getTamanhoTexto()));
        auditar(emailNormalizado, "contraste", String.valueOf(usuario.getContraste()), String.valueOf(request.getContraste()));
        auditar(emailNormalizado, "aparencia", String.valueOf(usuario.getAparencia()), String.valueOf(request.getAparencia()));
        auditar(emailNormalizado, "espacamento", String.valueOf(usuario.getEspacamento()), String.valueOf(request.getEspacamento()));
        auditar(emailNormalizado, "guia_leitura", String.valueOf(usuario.getGuiaLeitura()), String.valueOf(request.getGuiaLeitura()));
        auditar(emailNormalizado, "naveg_teclado", String.valueOf(usuario.getNavegTeclado()), String.valueOf(request.getNavegTeclado()));

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

        return auditoriaRepository.findByUsuarioEmailOrderByDataAlteracaoDesc(emailNormalizado);
    }

    private void validarTokenDaRequisicao(String email, String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token não informado.");
        }

        String token = authorizationHeader.replace("Bearer ", "");

        UsuarioPerfil usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (usuario.getToken() == null || !usuario.getToken().equals(token)) {
            throw new RuntimeException("Token inválido.");
        }

        if (usuario.getTokenExpiraEm() == null || usuario.getTokenExpiraEm().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado. Faça login novamente.");
        }

        if (!jwtService.tokenValido(token, email)) {
            throw new RuntimeException("Token inválido ou expirado.");
        }
    }

    private void validarEmailCaixa(String email) {
        if (email == null || !email.toLowerCase().endsWith("@caixa.gov.br")) {
            throw new RuntimeException("Apenas e-mails @caixa.gov.br são permitidos.");
        }
    }

    private void auditar(String email, String campo, String valorAnterior, String valorNovo) {
        if (valorAnterior == null && valorNovo == null) {
            return;
        }

        if (valorAnterior != null && valorAnterior.equals(valorNovo)) {
            return;
        }

        AuditoriaPerfil auditoria = new AuditoriaPerfil();
        auditoria.setUsuarioEmail(email.toLowerCase());
        auditoria.setCampoAlterado(campo);
        auditoria.setValorAnterior(valorAnterior);
        auditoria.setValorNovo(valorNovo);

        auditoriaRepository.save(auditoria);
    }
}