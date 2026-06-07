package globalsolution.ignis_orbital.repository;

import globalsolution.ignis_orbital.entity.Alerta;
import globalsolution.ignis_orbital.entity.NivelRisco;
import globalsolution.ignis_orbital.entity.StatusAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    List<Alerta> findByDsStatus(StatusAlerta status);

    List<Alerta> findByDsRisco(NivelRisco risco);

    @Query("SELECT a FROM Alerta a JOIN FETCH a.regiao JOIN FETCH a.satelite ORDER BY a.dtCaptura DESC")
    List<Alerta> findAllComRelacionamentos();

    @Query("SELECT AVG(a.vlTemperatura) FROM Alerta a WHERE a.regiao.id = :regiaoId")
    Double calcularTemperaturaMediaPorRegiao(Long regiaoId);

    @Query("SELECT a FROM Alerta a JOIN FETCH a.regiao JOIN FETCH a.satelite WHERE a.id = :id")
    java.util.Optional<Alerta> findByIdComRelacionamentos(Long id);

}
