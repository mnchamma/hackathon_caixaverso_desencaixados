package com.acessibilidade.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ErroResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String erro;
    private final List<String> detalhes;

    public ErroResponse(int status, String erro, List<String> detalhes) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.erro = erro;
        this.detalhes = detalhes;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getErro() {
        return erro;
    }

    public List<String> getDetalhes() {
        return detalhes;
    }
}
