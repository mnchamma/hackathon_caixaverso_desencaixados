package com.acessibilidade.api.service;

import com.acessibilidade.api.dto.LoginRequest;
import com.acessibilidade.api.dto.LoginResponse;
import com.acessibilidade.api.exception.ApiException;
import com.acessibilidade.api.model.UsuarioPerfil;
import com.acessibilidade.api.repository.UsuarioPerfilRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UsuarioPerfilRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuditoriaService auditoriaService;

    public AuthService(
            UsuarioPerfilRepository usuarioRepository,
            BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuditoriaService auditoriaService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.auditoriaService = auditoriaService;
    }

    public LoginResponse login(LoginRequest request) {
        validarEmailCaixa(request.getEmail());

        String emailNormalizado = request.getEmail().toLowerCase();

        UsuarioPerfil usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "E-mail ou senha invalidos."));

        boolean senhaValida = passwordEncoder.matches(
                request.getSenha(),
                usuario.getSenha()
        );

        if (!senhaValida) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "E-mail ou senha invalidos.");
        }

        String token = jwtService.gerarToken(usuario.getEmail());
        LocalDateTime tokenExpiraEm = jwtService.calcularExpiracao();

        usuario.setToken(token);
        usuario.setTokenExpiraEm(tokenExpiraEm);

        usuarioRepository.save(usuario);

        auditoriaService.registrar(
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

    private void validarEmailCaixa(String email) {
        if (email == null || !email.toLowerCase().endsWith("@caixa.gov.br")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Apenas e-mails @caixa.gov.br sao permitidos.");
        }
    }
}
