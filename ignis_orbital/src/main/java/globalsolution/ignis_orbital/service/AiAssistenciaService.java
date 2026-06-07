package globalsolution.ignis_orbital.service;

import globalsolution.ignis_orbital.ai.IgnisMonitoringTools;
import globalsolution.ignis_orbital.dto.AiConsultaRequest;
import globalsolution.ignis_orbital.dto.AiConsultaResponse;
import globalsolution.ignis_orbital.repository.AlertaRepository;
import globalsolution.ignis_orbital.repository.RegiaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiAssistenciaService {

    private final AlertaRepository alertaRepository;
    private final RegiaoRepository regiaoRepository;
    private final ObjectProvider<ChatClient.Builder> chatClientBuilderProvider;
    private final IgnisMonitoringTools monitoringTools;

    private ChatClient chatClient;

    public AiAssistenciaService(
            AlertaRepository alertaRepository,
            RegiaoRepository regiaoRepository,
            ObjectProvider<ChatClient.Builder> chatClientBuilderProvider,
            IgnisMonitoringTools monitoringTools
    ) {
        this.alertaRepository = alertaRepository;
        this.regiaoRepository = regiaoRepository;
        this.chatClientBuilderProvider = chatClientBuilderProvider;
        this.monitoringTools = monitoringTools;
    }

    public AiConsultaResponse consultar(AiConsultaRequest request) {
        ChatClient client = obterChatClient();

        if (client != null) {
            try {
                String resposta = client.prompt()
                        .system("""
                                Voce e o assistente Ignis Orbital especializado em prevencao de queimadas.
                                Use as ferramentas disponiveis para responder com dados reais do sistema
                                (alertas por risco, regioes monitoradas e indice de monitoramento).
                                Responda em portugues de forma objetiva.""")
                        .user(request.pergunta())
                        .call()
                        .content();
                return new AiConsultaResponse(resposta, true);
            } catch (Exception ex) {
                log.warn("Falha ao consultar Spring AI, retornando modo offline: {}", ex.getMessage());
                return new AiConsultaResponse(fallback(request) + "\n(Erro IA: " + ex.getMessage() + ")", false);
            }
        }

        return new AiConsultaResponse(fallback(request), false);
    }

    private ChatClient obterChatClient() {
        if (chatClient == null) {
            ChatClient.Builder builder = chatClientBuilderProvider.getIfAvailable();
            if (builder != null) {
                chatClient = builder.defaultTools(monitoringTools).build();
            }
        }
        return chatClient;
    }

    private String fallback(AiConsultaRequest request) {
        long totalAlertas = alertaRepository.count();
        long totalRegioes = regiaoRepository.count();
        return """
                Modo offline (Spring AI indisponivel - configure OPENAI_API_KEY e AI_CHAT=openai).
                Contexto RAG simplificado: o Ignis Orbital monitora %d regioes e possui %d alertas registrados.
                Pergunta recebida: %s
                """.formatted(totalRegioes, totalAlertas, request.pergunta()).trim();
    }
}
