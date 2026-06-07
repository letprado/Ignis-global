package globalsolution.ignis_orbital.service;

import globalsolution.ignis_orbital.dto.RegiaoRequest;
import globalsolution.ignis_orbital.dto.RegiaoResponse;
import globalsolution.ignis_orbital.entity.Regiao;
import globalsolution.ignis_orbital.exception.RecursoNaoEncontradoException;
import globalsolution.ignis_orbital.repository.AlertaRepository;
import globalsolution.ignis_orbital.repository.RegiaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegiaoService {

    private final RegiaoRepository regiaoRepository;
    private final AlertaRepository alertaRepository;

    @Cacheable("regioes")
    public List<RegiaoResponse> listarTodas() {
        return regiaoRepository.findAll().stream()
                .map(this::toResponseSimples)
                .toList();
    }

    @Cacheable(value = "regiao", key = "#id")
    public RegiaoResponse buscarPorId(Long id) {
        RegiaoResponse response = toResponseSimples(buscarEntidade(id));
        response.setIndiceMonitoramento(calcularIndiceMonitoramento(id));
        return response;
    }

    @Transactional
    @CacheEvict(value = {"regioes", "regiao"}, allEntries = true)
    public RegiaoResponse criar(RegiaoRequest request) {
        Regiao regiao = Regiao.builder()
                .nmRegiao(request.nmRegiao())
                .dsBioma(request.dsBioma())
                .nrCriticidadeBase(request.nrCriticidadeBase())
                .sgUf(request.sgUf().toUpperCase())
                .build();
        return toResponseSimples(regiaoRepository.save(regiao));
    }

    @Transactional
    @CacheEvict(value = {"regioes", "regiao"}, allEntries = true)
    public RegiaoResponse atualizar(Long id, RegiaoRequest request) {
        Regiao regiao = buscarEntidade(id);
        regiao.setNmRegiao(request.nmRegiao());
        regiao.setDsBioma(request.dsBioma());
        regiao.setNrCriticidadeBase(request.nrCriticidadeBase());
        regiao.setSgUf(request.sgUf().toUpperCase());
        return toResponseSimples(regiaoRepository.save(regiao));
    }

    @Transactional
    @CacheEvict(value = {"regioes", "regiao"}, allEntries = true)
    public void excluir(Long id) {
        if (!regiaoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Regiao nao encontrada: " + id);
        }
        regiaoRepository.deleteById(id);
    }

    public double calcularIndiceMonitoramento(Long regiaoId) {
        Regiao regiao = buscarEntidade(regiaoId);
        Double tempMedia = alertaRepository.calcularTemperaturaMediaPorRegiao(regiaoId);
        double media = tempMedia != null ? tempMedia : 0.0;
        return Math.round((regiao.getNrCriticidadeBase() * 0.4 + media * 0.6) * 100.0) / 100.0;
    }

    public Regiao buscarEntidade(Long id) {
        return regiaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Regiao nao encontrada: " + id));
    }

    private RegiaoResponse toResponseSimples(Regiao regiao) {
        RegiaoResponse response = new RegiaoResponse();
        response.setId(regiao.getId());
        response.setNmRegiao(regiao.getNmRegiao());
        response.setDsBioma(regiao.getDsBioma());
        response.setNrCriticidadeBase(regiao.getNrCriticidadeBase());
        response.setSgUf(regiao.getSgUf());
        return response;
    }
}
