package globalsolution.ignis_orbital.controller;

import globalsolution.ignis_orbital.dto.TelemetriaResponse;
import globalsolution.ignis_orbital.service.TelemetriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/telemetria")
@RequiredArgsConstructor
@Tag(name = "Telemetria")
@SecurityRequirement(name = "Bearer Authentication")
public class TelemetriaController {

    private final TelemetriaService telemetriaService;

    @GetMapping("/pendentes")
    @Operation(summary = "Listar payloads de telemetria pendentes de processamento")
    public List<TelemetriaResponse> listarPendentes() {
        return telemetriaService.listarPendentes();
    }

    @PostMapping("/{id}/processar")
    @Operation(summary = "Processar payload sincronamente e gerar alerta")
    public TelemetriaResponse processar(@PathVariable Long id) {
        return telemetriaService.processarPayload(id);
    }

    @PostMapping("/{id}/processar-async")
    @Operation(summary = "Enfileirar processamento assincrono via RabbitMQ")
    public ResponseEntity<Map<String, String>> processarAsync(@PathVariable Long id) {
        telemetriaService.solicitarProcessamentoAssincrono(id);
        return ResponseEntity.accepted().body(Map.of(
                "mensagem", "Processamento enfileirado",
                "payloadId", id.toString()
        ));
    }
}
