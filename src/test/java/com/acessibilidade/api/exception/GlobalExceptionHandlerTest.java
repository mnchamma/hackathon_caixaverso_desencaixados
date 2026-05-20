package com.acessibilidade.api.exception;
import com.acessibilidade.api.dto.ErroResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.lang.reflect.Method;
import static org.assertj.core.api.Assertions.assertThat;
class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    @Test
    void handleApiException() {
        ApiException ex = new ApiException(HttpStatus.NOT_FOUND, "nao encontrado");
        ResponseEntity<ErroResponse> resp = handler.handleApiException(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getStatus()).isEqualTo(404);
        assertThat(resp.getBody().getErro()).isEqualTo("Not Found");
        assertThat(resp.getBody().getDetalhes()).contains("nao encontrado");
        assertThat(resp.getBody().getTimestamp()).isNotNull();
    }
    @Test
    void handleUnexpected() {
        ResponseEntity<ErroResponse> resp = handler.handleUnexpected(new RuntimeException("boom"));
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getDetalhes()).hasSize(1);
    }
    @Test
    void handleValidation() throws Exception {
        BeanPropertyBindingResult binding = new BeanPropertyBindingResult(new Object(), "obj");
        binding.addError(new FieldError("obj", "email", "nao pode ser vazio"));
        Method m = String.class.getMethod("toString");
        MethodParameter param = new MethodParameter(m, -1);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(param, binding);
        ResponseEntity<ErroResponse> resp = handler.handleValidation(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getDetalhes()).contains("email: nao pode ser vazio");
    }
    @Test
    void apiExceptionMantemStatusEMessage() {
        ApiException ex = new ApiException(HttpStatus.CONFLICT, "duplicado");
        assertThat(ex.getStatus()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(ex.getMessage()).isEqualTo("duplicado");
    }
}
