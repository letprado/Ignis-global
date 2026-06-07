package globalsolution.ignis_orbital.controller;

import globalsolution.ignis_orbital.dto.AiConsultaRequest;
import globalsolution.ignis_orbital.dto.AiConsultaResponse;
import globalsolution.ignis_orbital.service.AiAssistenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "Inteligencia Artificial")
@SecurityRequirement(name = "Bearer Authentication")
public class AiController {

    private final AiAssistenciaService aiAssistenciaService;

    @PostMapping("/consultar")
    @Operation(summary = "Consultar assistente Ignis com Tooling Spring AI")
    public AiConsultaResponse consultar(@Valid @RequestBody AiConsultaRequest request) {
        return aiAssistenciaService.consultar(request);
    }
}
