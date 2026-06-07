package globalsolution.ignis_orbital.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "openMeteoClient", url = "${ignis.openmeteo.base-url}")
public interface OpenMeteoClient {

    @GetMapping("/v1/forecast")
    Map<String, Object> obterPrevisao(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("current") String current,
            @RequestParam("timezone") String timezone
    );
}
