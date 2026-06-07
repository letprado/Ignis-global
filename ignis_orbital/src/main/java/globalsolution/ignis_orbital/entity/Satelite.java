package globalsolution.ignis_orbital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "GS_SATELITES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Satelite {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_satelite")
    @SequenceGenerator(name = "seq_satelite", sequenceName = "SEQ_SATELITE", allocationSize = 1)
    @Column(name = "ID_SATELITE")
    private Long id;

    @Column(name = "NM_SATELITE", nullable = false, unique = true, length = 100)
    private String nmSatelite;

    @Column(name = "DS_SENSOR", nullable = false, length = 100)
    private String dsSensor;

    @Column(name = "DS_ORBITA", length = 50)
    private String dsOrbita;

    @Column(name = "DT_LANCAMENTO")
    private LocalDate dtLancamento;
}
