package globalsolution.ignis_orbital.service;

import globalsolution.ignis_orbital.client.OpenMeteoClient;
import globalsolution.ignis_orbital.entity.Alerta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClimaContextoService {

    private final OpenMeteoClient openMeteoClient;

    public Map<String, Object> enriquecerAlertaComClima(Alerta alerta) {
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("alertaId", alerta.getId());
        resultado.put("regiao", alerta.getRegiao().getNmRegiao());
        resultado.put("temperaturaSatelite", alerta.getVlTemperatura());

        try {
            Map<String, Object> previsao = openMeteoClient.obterPrevisao(
                    alerta.getNrLatitude().doubleValue(),
                    alerta.getNrLongitude().doubleValue(),
                    "temperature_2m,relative_humidity_2m,wind_speed_10m",
                    "America/Sao_Paulo"
            );
            resultado.put("climaLocal", previsao);
            resultado.put("fonte", "Open-Meteo via Feign");
        } catch (Exception ex) {
            resultado.put("climaLocal", Map.of("erro", "Servico climatico indisponivel"));
        }

        return resultado;
    }
}
