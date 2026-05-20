package com.acessibilidade.api.service;

import com.acessibilidade.api.dto.AtualizarPreferenciasRequest;
import com.acessibilidade.api.dto.CriarUsuarioRequest;
import com.acessibilidade.api.model.AuditoriaPerfil;
import com.acessibilidade.api.model.UsuarioPerfil;
import com.acessibilidade.api.repository.AuditoriaPerfilRepository;
import com.acessibilidade.api.repository.UsuarioPerfilRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioPerfilService {

    private final UsuarioPerfilRepository usuarioRepository;
    private final AuditoriaPerfilRepository auditoriaRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioPerfilService(
            UsuarioPerfilRepository usuarioRepository,
            AuditoriaPerfilRepository auditoriaRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.auditoriaRepository = auditoriaRepository;
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
        usuario.setPreferencia(request.getPreferencia());
        usuario.setTamanhoTexto(request.getTamanhoTexto());
        usuario.setContraste(request.getContraste());
        usuario.setAparencia(request.getAparencia());
        usuario.setEspacamento(request.getEspacamento());
        usuario.setDestaque(Boolean.TRUE.equals(request.getDestaque()));
        usuario.setNavegTeclado(Boolean.TRUE.equals(request.getNavegTeclado()));
        usuario.setInterfaceSimplif(Boolean.TRUE.equals(request.getInterfaceSimplif()));

        return usuarioRepository.save(usuario);
    }

    public UsuarioPerfil buscarPorEmail(String email) {
        String emailNormalizado = email.toLowerCase();

        return usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }

    public UsuarioPerfil atualizarPreferencias(String email, AtualizarPreferenciasRequest request) {
        validarEmailCaixa(email);

        String emailNormalizado = email.toLowerCase();

        UsuarioPerfil usuario = buscarPorEmail(emailNormalizado);

        auditar(emailNormalizado, "preferencia", usuario.getPreferencia(), request.getPreferencia());
        auditar(emailNormalizado, "tamanho_texto", String.valueOf(usuario.getTamanhoTexto()), String.valueOf(request.getTamanhoTexto()));
        auditar(emailNormalizado, "contraste", usuario.getContraste(), request.getContraste());
        auditar(emailNormalizado, "aparencia", usuario.getAparencia(), request.getAparencia());
        auditar(emailNormalizado, "espacamento", usuario.getEspacamento(), request.getEspacamento());
        auditar(emailNormalizado, "destaque", String.valueOf(usuario.getDestaque()), String.valueOf(request.getDestaque()));
        auditar(emailNormalizado, "naveg_teclado", String.valueOf(usuario.getNavegTeclado()), String.valueOf(request.getNavegTeclado()));
        auditar(emailNormalizado, "interface_simplif", String.valueOf(usuario.getInterfaceSimplif()), String.valueOf(request.getInterfaceSimplif()));

        usuario.setPreferencia(request.getPreferencia());
        usuario.setTamanhoTexto(request.getTamanhoTexto());
        usuario.setContraste(request.getContraste());
        usuario.setAparencia(request.getAparencia());
        usuario.setEspacamento(request.getEspacamento());
        usuario.setDestaque(Boolean.TRUE.equals(request.getDestaque()));
        usuario.setNavegTeclado(Boolean.TRUE.equals(request.getNavegTeclado()));
        usuario.setInterfaceSimplif(Boolean.TRUE.equals(request.getInterfaceSimplif()));

        return usuarioRepository.save(usuario);
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
        auditoria.setUsuarioEmail(email);
        auditoria.setCampoAlterado(campo);
        auditoria.setValorAnterior(valorAnterior);
        auditoria.setValorNovo(valorNovo);

        auditoriaRepository.save(auditoria);
    }
    
    public List<AuditoriaPerfil> buscarAuditoriaPorUsuario(String email) {
        validarEmailCaixa(email);

        return auditoriaRepository.findByUsuarioEmailOrderByDataAlteracaoDesc(email.toLowerCase());
    }
}