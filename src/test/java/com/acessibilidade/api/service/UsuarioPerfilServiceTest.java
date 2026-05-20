package com.acessibilidade.api.service;
import com.acessibilidade.api.dto.AtualizarPreferenciasRequest;
import com.acessibilidade.api.dto.BuscarPerfilRequest;
import com.acessibilidade.api.dto.CriarUsuarioRequest;
import com.acessibilidade.api.exception.ApiException;
import com.acessibilidade.api.model.AuditoriaPerfil;
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
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UsuarioPerfilServiceTest {
    @Mock private UsuarioPerfilRepository usuarioRepository;
    @Mock private AuditoriaService auditoriaService;
    @Mock private BCryptPasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @InjectMocks private UsuarioPerfilService service;
    private UsuarioPerfil usuarioExistente;
    @BeforeEach
    void setUp() {
        usuarioExistente = new UsuarioPerfil();
        usuarioExistente.setEmail("user@caixa.gov.br");
        usuarioExistente.setSenha("hash");
        usuarioExistente.setTamanhoTexto(14);
        usuarioExistente.setContraste(false);
        usuarioExistente.setAparencia(false);
        usuarioExistente.setEspacamento(1.0f);
        usuarioExistente.setGuiaLeitura(false);
        usuarioExistente.setNavegTeclado(false);
        usuarioExistente.setToken("token-valido");
        usuarioExistente.setTokenExpiraEm(LocalDateTime.now().plusHours(1));
    }
    @Test
    void criarUsuarioComSucesso() {
        CriarUsuarioRequest req = novoCriarRequest("New@caixa.gov.br");
        when(usuarioRepository.existsByEmail("new@caixa.gov.br")).thenReturn(false);
        when(passwordEncoder.encode("senha")).thenReturn("hash");
        when(usuarioRepository.save(any(UsuarioPerfil.class))).thenAnswer(inv -> inv.getArgument(0));
        UsuarioPerfil result = service.criarUsuario(req);
        assertThat(result.getEmail()).isEqualTo("new@caixa.gov.br");
        assertThat(result.getSenha()).isEqualTo("hash");
        assertThat(result.getTamanhoTexto()).isEqualTo(14);
        verify(auditoriaService).registrar(eq("new@caixa.gov.br"), eq("CADASTRO"), isNull(), anyString());
    }
    @Test
    void criarUsuarioFalhaEmailInvalido() {
        CriarUsuarioRequest req = novoCriarRequest("a@gmail.com");
        assertThatThrownBy(() -> service.criarUsuario(req))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("@caixa.gov.br");
    }
    @Test
    void criarUsuarioFalhaEmailJaExiste() {
        CriarUsuarioRequest req = novoCriarRequest("a@caixa.gov.br");
        when(usuarioRepository.existsByEmail("a@caixa.gov.br")).thenReturn(true);
        assertThatThrownBy(() -> service.criarUsuario(req))
                .isInstanceOf(ApiException.class)
                .satisfies(e -> assertThat(((ApiException) e).getStatus()).isEqualTo(HttpStatus.CONFLICT));
    }
    @Test
    void criarUsuarioBooleansNullViramFalse() {
        CriarUsuarioRequest req = novoCriarRequest("x@caixa.gov.br");
        req.setContraste(null);
        req.setAparencia(null);
        req.setGuiaLeitura(null);
        req.setNavegTeclado(null);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hash");
        when(usuarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        UsuarioPerfil result = service.criarUsuario(req);
        assertThat(result.getContraste()).isFalse();
        assertThat(result.getAparencia()).isFalse();
        assertThat(result.getGuiaLeitura()).isFalse();
        assertThat(result.getNavegTeclado()).isFalse();
    }
    @Test
    void buscarPorEmailComSucesso() {
        BuscarPerfilRequest req = new BuscarPerfilRequest();
        req.setEmail("USER@caixa.gov.br");
        when(usuarioRepository.findByEmail("user@caixa.gov.br")).thenReturn(Optional.of(usuarioExistente));
        when(jwtService.tokenValido("token-valido", "user@caixa.gov.br")).thenReturn(true);
        UsuarioPerfil result = service.buscarPorEmail(req, "Bearer token-valido");
        assertThat(result).isSameAs(usuarioExistente);
    }
    @Test
    void buscarPorEmailFalhaEmailInvalido() {
        BuscarPerfilRequest req = new BuscarPerfilRequest();
        req.setEmail("u@gmail.com");
        assertThatThrownBy(() -> service.buscarPorEmail(req, "Bearer x"))
                .isInstanceOf(ApiException.class);
    }
    @Test
    void buscarPorEmailUsuarioNaoEncontrado() {
        BuscarPerfilRequest req = new BuscarPerfilRequest();
        req.setEmail("user@caixa.gov.br");
        when(usuarioRepository.findByEmail("user@caixa.gov.br")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.buscarPorEmail(req, "Bearer x"))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Usuario nao encontrado");
    }
    @Test
    void tokenAusenteFalha() {
        BuscarPerfilRequest req = new BuscarPerfilRequest();
        req.setEmail("user@caixa.gov.br");
        assertThatThrownBy(() -> service.buscarPorEmail(req, null))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Token nao informado");
    }
    @Test
    void tokenSemBearerFalha() {
        BuscarPerfilRequest req = new BuscarPerfilRequest();
        req.setEmail("user@caixa.gov.br");
        assertThatThrownBy(() -> service.buscarPorEmail(req, "abc"))
                .isInstanceOf(ApiException.class);
    }
    @Test
    void tokenNaoBateComUsuario() {
        BuscarPerfilRequest req = new BuscarPerfilRequest();
        req.setEmail("user@caixa.gov.br");
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioExistente));
        assertThatThrownBy(() -> service.buscarPorEmail(req, "Bearer outro"))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Token invalido");
    }
    @Test
    void tokenSemTokenNoUsuarioFalha() {
        usuarioExistente.setToken(null);
        BuscarPerfilRequest req = new BuscarPerfilRequest();
        req.setEmail("user@caixa.gov.br");
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioExistente));
        assertThatThrownBy(() -> service.buscarPorEmail(req, "Bearer qq"))
                .isInstanceOf(ApiException.class);
    }
    @Test
    void tokenExpiradoFalha() {
        usuarioExistente.setTokenExpiraEm(LocalDateTime.now().minusMinutes(1));
        BuscarPerfilRequest req = new BuscarPerfilRequest();
        req.setEmail("user@caixa.gov.br");
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioExistente));
        assertThatThrownBy(() -> service.buscarPorEmail(req, "Bearer token-valido"))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("expirado");
    }
    @Test
    void tokenSemDataExpiracaoFalha() {
        usuarioExistente.setTokenExpiraEm(null);
        BuscarPerfilRequest req = new BuscarPerfilRequest();
        req.setEmail("user@caixa.gov.br");
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioExistente));
        assertThatThrownBy(() -> service.buscarPorEmail(req, "Bearer token-valido"))
                .isInstanceOf(ApiException.class);
    }
    @Test
    void jwtServiceConsideraTokenInvalido() {
        BuscarPerfilRequest req = new BuscarPerfilRequest();
        req.setEmail("user@caixa.gov.br");
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioExistente));
        when(jwtService.tokenValido(anyString(), anyString())).thenReturn(false);
        assertThatThrownBy(() -> service.buscarPorEmail(req, "Bearer token-valido"))
                .isInstanceOf(ApiException.class);
    }
    @Test
    void atualizarPreferenciasComSucesso() {
        AtualizarPreferenciasRequest req = new AtualizarPreferenciasRequest();
        req.setEmail("user@caixa.gov.br");
        req.setTamanhoTexto(20);
        req.setContraste(true);
        req.setAparencia(true);
        req.setEspacamento(2.0f);
        req.setGuiaLeitura(true);
        req.setNavegTeclado(true);
        when(usuarioRepository.findByEmail("user@caixa.gov.br")).thenReturn(Optional.of(usuarioExistente));
        when(jwtService.tokenValido(anyString(), anyString())).thenReturn(true);
        when(usuarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        UsuarioPerfil result = service.atualizarPreferencias(req, "Bearer token-valido");
        assertThat(result.getTamanhoTexto()).isEqualTo(20);
        assertThat(result.getContraste()).isTrue();
        assertThat(result.getAparencia()).isTrue();
        assertThat(result.getEspacamento()).isEqualTo(2.0f);
        verify(auditoriaService, times(6)).registrar(anyString(), anyString(), any(), any());
    }
    @Test
    void atualizarPreferenciasFalhaUsuarioNaoEncontrado() {
        AtualizarPreferenciasRequest req = new AtualizarPreferenciasRequest();
        req.setEmail("user@caixa.gov.br");
        req.setTamanhoTexto(20);
        req.setContraste(true);
        req.setAparencia(true);
        req.setEspacamento(2.0f);
        req.setGuiaLeitura(true);
        req.setNavegTeclado(true);
        when(usuarioRepository.findByEmail("user@caixa.gov.br")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.atualizarPreferencias(req, "Bearer token-valido"))
                .isInstanceOf(ApiException.class);
    }
    @Test
    void buscarAuditoriaComSucesso() {
        BuscarPerfilRequest req = new BuscarPerfilRequest();
        req.setEmail("user@caixa.gov.br");
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioExistente));
        when(jwtService.tokenValido(anyString(), anyString())).thenReturn(true);
        when(auditoriaService.buscarPorUsuario("user@caixa.gov.br"))
                .thenReturn(List.of(new AuditoriaPerfil()));
        List<AuditoriaPerfil> result = service.buscarAuditoriaPorUsuario(req, "Bearer token-valido");
        assertThat(result).hasSize(1);
    }
    private CriarUsuarioRequest novoCriarRequest(String email) {
        CriarUsuarioRequest req = new CriarUsuarioRequest();
        req.setEmail(email);
        req.setSenha("senha");
        req.setTamanhoTexto(14);
        req.setContraste(true);
        req.setAparencia(false);
        req.setEspacamento(1.0f);
        req.setGuiaLeitura(true);
        req.setNavegTeclado(false);
        return req;
    }
}
