package globalsolution.ignis_orbital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "GS_ALERTAS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_alerta")
    @SequenceGenerator(name = "seq_alerta", sequenceName = "SEQ_ALERTA", allocationSize = 1)
    @Column(name = "ID_ALERTA")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_REGIAO", nullable = false)
    private Regiao regiao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SATELITE", nullable = false)
    private Satelite satelite;

    @Column(name = "NR_LATITUDE", nullable = false, precision = 10, scale = 6)
    private BigDecimal nrLatitude;

    @Column(name = "NR_LONGITUDE", nullable = false, precision = 10, scale = 6)
    private BigDecimal nrLongitude;

    @Column(name = "VL_TEMPERATURA", nullable = false, precision = 5, scale = 2)
    private BigDecimal vlTemperatura;

    @Enumerated(EnumType.STRING)
    @Column(name = "DS_RISCO", nullable = false, length = 20)
    private NivelRisco dsRisco;

    @Enumerated(EnumType.STRING)
    @Column(name = "DS_STATUS", nullable = false, length = 20)
    private StatusAlerta dsStatus;

    @Column(name = "DT_CAPTURA", nullable = false)
    private LocalDateTime dtCaptura;
}
