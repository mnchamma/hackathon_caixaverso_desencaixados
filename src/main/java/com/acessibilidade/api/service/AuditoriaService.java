package com.acessibilidade.api.service;

import com.acessibilidade.api.model.AuditoriaPerfil;
import com.acessibilidade.api.repository.AuditoriaPerfilRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriaService {

    private final AuditoriaPerfilRepository auditoriaRepository;

    public AuditoriaService(AuditoriaPerfilRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    public void registrar(String email, String campo, String valorAnterior, String valorNovo) {
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

    public List<AuditoriaPerfil> buscarPorUsuario(String email) {
        return auditoriaRepository.findByUsuarioEmailOrderByDataAlteracaoDesc(email.toLowerCase());
    }
}
