package globalsolution.ignis_orbital.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GS_REGIOES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Regiao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_regiao")
    @SequenceGenerator(name = "seq_regiao", sequenceName = "SEQ_REGIAO", allocationSize = 1)
    @Column(name = "ID_REGIAO")
    private Long id;

    @Column(name = "NM_REGIAO", nullable = false, length = 100)
    private String nmRegiao;

    @Enumerated(EnumType.STRING)
    @Column(name = "DS_BIOMA", nullable = false, length = 50)
    private Bioma dsBioma;

    @Column(name = "NR_CRITICIDADE_BASE", nullable = false)
    private Integer nrCriticidadeBase;

    @Column(name = "SG_UF", nullable = false, length = 2)
    private String sgUf;
}
