package com.acessibilidade.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiApplicationTests {

	@Test
	void mainClassPodeSerInstanciada() {
		// Smoke test simples — evita carregar contexto do Spring (que exige Postgres).
		assertThat(new AcessibilidadeApiApplication()).isNotNull();
	}

}
