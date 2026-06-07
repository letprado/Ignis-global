package globalsolution.ignis_orbital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "GS_HISTORICO_LOG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_log")
    @SequenceGenerator(name = "seq_log", sequenceName = "SEQ_LOG", allocationSize = 1)
    @Column(name = "ID_LOG")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ALERTA")
    private Alerta alerta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    @Column(name = "DS_ACAO", nullable = false, length = 255)
    private String dsAcao;

    @Column(name = "DT_REGISTRO", nullable = false)
    private LocalDateTime dtRegistro;
}
