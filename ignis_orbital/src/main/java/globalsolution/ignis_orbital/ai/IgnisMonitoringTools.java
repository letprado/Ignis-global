package globalsolution.ignis_orbital.ai;

import globalsolution.ignis_orbital.entity.NivelRisco;
import globalsolution.ignis_orbital.repository.AlertaRepository;
import globalsolution.ignis_orbital.repository.RegiaoRepository;
import globalsolution.ignis_orbital.service.RegiaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IgnisMonitoringTools {

    private final AlertaRepository alertaRepository;
    private final RegiaoRepository regiaoRepository;
    private final RegiaoService regiaoService;

    @Tool(description = "Conta alertas ativos por nivel de risco no ecossistema Ignis Orbital")
    public long contarAlertasPorRisco(
            @ToolParam(description = "Nivel de risco: ALTO, MEDIO ou BAIXO") String risco
    ) {
        return alertaRepository.findByDsRisco(NivelRisco.valueOf(risco)).size();
    }

    @Tool(description = "Calcula indice de monitoramento de uma regiao considerando criticidade e temperatura media")
    public double calcularIndiceRegiao(
            @ToolParam(description = "Identificador numerico da regiao") Long regiaoId
    ) {
        return regiaoService.calcularIndiceMonitoramento(regiaoId);
    }

    @Tool(description = "Lista nomes de todas as regioes monitoradas")
    public String listarRegioesMonitoradas() {
        return regiaoRepository.findAll().stream()
                .map(r -> r.getNmRegiao() + " (" + r.getDsBioma() + ")")
                .reduce((a, b) -> a + "; " + b)
                .orElse("Nenhuma regiao cadastrada");
    }
}
