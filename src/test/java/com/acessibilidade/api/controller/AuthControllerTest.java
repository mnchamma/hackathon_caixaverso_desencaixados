package com.acessibilidade.api.controller;
import com.acessibilidade.api.dto.LoginRequest;
import com.acessibilidade.api.dto.LoginResponse;
import com.acessibilidade.api.exception.ApiException;
import com.acessibilidade.api.exception.GlobalExceptionHandler;
import com.acessibilidade.api.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
class AuthControllerTest {
    private AuthService service;
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    @BeforeEach
    void setUp() {
        service = Mockito.mock(AuthService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }
    @Test
    void loginOk() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail("a@caixa.gov.br");
        req.setSenha("123");
        LoginResponse resp = new LoginResponse("ok", "tok", LocalDateTime.now(),
                "a@caixa.gov.br", 14, true, false, 1.0f, true, false);
        when(service.login(any())).thenReturn(resp);
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("tok"))
                .andExpect(jsonPath("$.config.preferencia").value("a@caixa.gov.br"));
    }
    @Test
    void loginFalha() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail("a@caixa.gov.br");
        req.setSenha("123");
        when(service.login(any())).thenThrow(new ApiException(HttpStatus.UNAUTHORIZED, "invalido"));
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }
}
