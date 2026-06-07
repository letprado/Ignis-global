package globalsolution.ignis_orbital.dto;

import globalsolution.ignis_orbital.entity.Bioma;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "regioes", itemRelation = "regiao")
public class RegiaoResponse extends RepresentationModel<RegiaoResponse> {

    private Long id;
    private String nmRegiao;
    private Bioma dsBioma;
    private Integer nrCriticidadeBase;
    private String sgUf;
    private Double indiceMonitoramento;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNmRegiao() { return nmRegiao; }
    public void setNmRegiao(String nmRegiao) { this.nmRegiao = nmRegiao; }
    public Bioma getDsBioma() { return dsBioma; }
    public void setDsBioma(Bioma dsBioma) { this.dsBioma = dsBioma; }
    public Integer getNrCriticidadeBase() { return nrCriticidadeBase; }
    public void setNrCriticidadeBase(Integer nrCriticidadeBase) { this.nrCriticidadeBase = nrCriticidadeBase; }
    public String getSgUf() { return sgUf; }
    public void setSgUf(String sgUf) { this.sgUf = sgUf; }
    public Double getIndiceMonitoramento() { return indiceMonitoramento; }
    public void setIndiceMonitoramento(Double indiceMonitoramento) { this.indiceMonitoramento = indiceMonitoramento; }
}
