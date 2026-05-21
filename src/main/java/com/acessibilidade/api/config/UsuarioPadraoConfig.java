package com.acessibilidade.api.config;

import com.acessibilidade.api.model.UsuarioPerfil;
import com.acessibilidade.api.repository.UsuarioPerfilRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UsuarioPadraoConfig {

    private static final String EMAIL_USUARIO_PADRAO = "usuario@caixa.gov.br";
    private static final String SENHA_USUARIO_PADRAO = "SenhaForte123";

    @Bean
    CommandLineRunner criarUsuarioPadrao(
            UsuarioPerfilRepository usuarioRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (usuarioRepository.existsByEmail(EMAIL_USUARIO_PADRAO)) {
                return;
            }

            UsuarioPerfil usuario = new UsuarioPerfil();
            usuario.setEmail(EMAIL_USUARIO_PADRAO);
            usuario.setSenha(passwordEncoder.encode(SENHA_USUARIO_PADRAO));
            usuario.setTamanhoTexto(16);
            usuario.setContraste(false);
            usuario.setAparencia(false);
            usuario.setEspacamento(1.0F);
            usuario.setGuiaLeitura(false);
            usuario.setNavegTeclado(false);

            usuarioRepository.save(usuario);
        };
    }
}
