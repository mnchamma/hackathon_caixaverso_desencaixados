package com.acessibilidade.api.service;
import com.acessibilidade.api.dto.LoginRequest;
import com.acessibilidade.api.dto.LoginResponse;
import com.acessibilidade.api.exception.ApiException;
import com.acessibilidade.api.model.UsuarioPerfil;
import com.acessibilidade.api.repository.UsuarioPerfilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock private UsuarioPerfilRepository usuarioRepository;
    @Mock private BCryptPasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuditoriaService auditoriaService;
    @InjectMocks private AuthService authService;
    private LoginRequest request;
    @BeforeEach
    void setUp() {
        request = new LoginRequest();
        request.setEmail("User@caixa.gov.br");
        request.setSenha("senha123");
    }
    @Test
    void loginComSucesso() {
        UsuarioPerfil usuario = new UsuarioPerfil();
        usuario.setEmail("user@caixa.gov.br");
        usuario.setSenha("hash");
        usuario.setTamanhoTexto(16);
        usuario.setContraste(true);
        usuario.setAparencia(false);
        usuario.setEspacamento(1.5f);
        usuario.setGuiaLeitura(true);
        usuario.setNavegTeclado(false);
        when(usuarioRepository.findByEmail("user@caixa.gov.br")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha123", "hash")).thenReturn(true);
        when(jwtService.gerarToken("user@caixa.gov.br")).thenReturn("token-jwt");
        LocalDateTime exp = LocalDateTime.now().plusHours(1);
        when(jwtService.calcularExpiracao()).thenReturn(exp);
        LoginResponse response = authService.login(request);
        assertThat(response.getToken()).isEqualTo("token-jwt");
        assertThat(response.getTokenExpiraEm()).isEqualTo(exp);
        assertThat(response.getConfig().getPreferencia()).isEqualTo("user@caixa.gov.br");
        assertThat(response.getConfig().getTamanhoTexto()).isEqualTo(16);
        assertThat(response.getMensagem()).isNotBlank();
        verify(usuarioRepository).save(usuario);
        verify(auditoriaService).registrar(eq("user@caixa.gov.br"), eq("LOGIN"), isNull(), anyString());
    }
    @Test
    void loginFalhaEmailNaoCaixa() {
        request.setEmail("user@gmail.com");
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("@caixa.gov.br");
    }
    @Test
    void loginFalhaEmailNull() {
        request.setEmail(null);
        assertThatThrownBy(() -> authService.login(request)).isInstanceOf(ApiException.class);
    }
    @Test
    void loginFalhaUsuarioNaoEncontrado() {
        when(usuarioRepository.findByEmail("user@caixa.gov.br")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(ApiException.class)
                .satisfies(e -> assertThat(((ApiException) e).getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED));
    }
    @Test
    void loginFalhaSenhaInvalida() {
        UsuarioPerfil usuario = new UsuarioPerfil();
        usuario.setEmail("user@caixa.gov.br");
        usuario.setSenha("hash");
        when(usuarioRepository.findByEmail("user@caixa.gov.br")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("invalidos");
    }
}
