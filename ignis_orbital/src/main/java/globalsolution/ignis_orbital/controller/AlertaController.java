package globalsolution.ignis_orbital.controller;

import globalsolution.ignis_orbital.dto.AlertaResponse;
import globalsolution.ignis_orbital.dto.AtualizarStatusAlertaRequest;
import globalsolution.ignis_orbital.service.AlertaService;
import globalsolution.ignis_orbital.service.ClimaContextoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
@Tag(name = "Alertas")
@SecurityRequirement(name = "Bearer Authentication")
public class AlertaController {

    private final AlertaService alertaService;
    private final ClimaContextoService climaContextoService;

    @GetMapping
    @Operation(summary = "Listar alertas de focos de calor detectados (rota publica)")
    @SecurityRequirements
    public List<AlertaResponse> listar() {
        return alertaService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar alerta por ID (rota publica)")
    @SecurityRequirements
    public AlertaResponse buscarPorId(@PathVariable Long id) {
        return alertaService.buscarPorId(id);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status operacional do alerta com auditoria")
    public AlertaResponse atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusAlertaRequest request
    ) {
        return alertaService.atualizarStatus(id, request);
    }

    @GetMapping("/{id}/contexto-climatico")
    @Operation(summary = "Enriquecer alerta com dados climaticos locais via Open-Meteo (Feign)")
    public ResponseEntity<Map<String, Object>> contextoClimatico(@PathVariable Long id) {
        var alerta = alertaService.buscarEntidade(id);
        return ResponseEntity.ok(climaContextoService.enriquecerAlertaComClima(alerta));
    }
}
