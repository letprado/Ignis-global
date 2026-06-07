package globalsolution.ignis_orbital.controller;

import globalsolution.ignis_orbital.controller.assembler.RegiaoModelAssembler;
import globalsolution.ignis_orbital.dto.RegiaoRequest;
import globalsolution.ignis_orbital.dto.RegiaoResponse;
import globalsolution.ignis_orbital.service.RegiaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/regioes")
@RequiredArgsConstructor
@Tag(name = "Regioes")
@SecurityRequirement(name = "Bearer Authentication")
public class RegiaoController {

    private final RegiaoService regiaoService;
    private final RegiaoModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar todas as regioes monitoradas")
    public CollectionModel<EntityModel<RegiaoResponse>> listar() {
        var regioes = regiaoService.listarTodas().stream()
                .map(assembler::toModel)
                .toList();
        return CollectionModel.of(regioes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar regiao por ID")
    public EntityModel<RegiaoResponse> buscarPorId(@PathVariable Long id) {
        return assembler.toModel(regiaoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar nova regiao")
    public ResponseEntity<EntityModel<RegiaoResponse>> criar(@Valid @RequestBody RegiaoRequest request) {
        RegiaoResponse criada = regiaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(criada));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar regiao existente")
    public EntityModel<RegiaoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody RegiaoRequest request) {
        return assembler.toModel(regiaoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir regiao")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        regiaoService.excluir(id);
    }

    @GetMapping("/{id}/indice-monitoramento")
    @Operation(summary = "Calcular indice composto de monitoramento da regiao")
    public Map<String, Object> calcularIndice(@PathVariable Long id) {
        double indice = regiaoService.calcularIndiceMonitoramento(id);
        return Map.of(
                "regiaoId", id,
                "indiceMonitoramento", indice,
                "descricao", "Indice = (criticidade_base * 0.4) + (temperatura_media_alertas * 0.6)"
        );
    }
}
