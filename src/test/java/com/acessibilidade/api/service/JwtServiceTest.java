package com.acessibilidade.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret",
                "uma-chave-secreta-super-segura-com-no-minimo-256-bits!!");
        ReflectionTestUtils.setField(jwtService, "expirationMinutes", 60L);
    }

    @Test
    void gerarTokenEExtrairEmail() {
        String token = jwtService.gerarToken("user@caixa.gov.br");
        assertThat(token).isNotBlank();
        assertThat(jwtService.extrairEmail(token)).isEqualTo("user@caixa.gov.br");
    }

    @Test
    void calcularExpiracaoFutura() {
        LocalDateTime exp = jwtService.calcularExpiracao();
        assertThat(exp).isAfter(LocalDateTime.now());
    }

    @Test
    void tokenValidoRetornaTrueParaEmailCorreto() {
        String token = jwtService.gerarToken("a@caixa.gov.br");
        assertThat(jwtService.tokenValido(token, "A@CAIXA.GOV.BR")).isTrue();
    }

    @Test
    void tokenValidoRetornaFalseParaEmailDiferente() {
        String token = jwtService.gerarToken("a@caixa.gov.br");
        assertThat(jwtService.tokenValido(token, "b@caixa.gov.br")).isFalse();
    }

    @Test
    void tokenValidoRetornaFalseParaTokenMalformado() {
        assertThat(jwtService.tokenValido("token-invalido", "a@caixa.gov.br")).isFalse();
    }
}

