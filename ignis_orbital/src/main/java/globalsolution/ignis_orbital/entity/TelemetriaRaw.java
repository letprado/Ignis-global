package globalsolution.ignis_orbital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "GS_TELEMETRIA_RAW")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelemetriaRaw {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_telemetria")
    @SequenceGenerator(name = "seq_telemetria", sequenceName = "SEQ_TELEMETRIA", allocationSize = 1)
    @Column(name = "ID_PAYLOAD")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SATELITE", nullable = false)
    private Satelite satelite;

    @Lob
    @Column(name = "JSON_DATA", nullable = false, columnDefinition = "CLOB")
    private String jsonData;

    @Column(name = "DT_RECEBIMENTO", nullable = false)
    private LocalDateTime dtRecebimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "DS_STATUS_PROC", nullable = false, length = 20)
    private StatusProcessamento dsStatusProc;
}
