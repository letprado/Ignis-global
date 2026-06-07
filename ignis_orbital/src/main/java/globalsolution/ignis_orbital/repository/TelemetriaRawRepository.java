package globalsolution.ignis_orbital.repository;

import globalsolution.ignis_orbital.entity.StatusProcessamento;
import globalsolution.ignis_orbital.entity.TelemetriaRaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TelemetriaRawRepository extends JpaRepository<TelemetriaRaw, Long> {
    List<TelemetriaRaw> findByDsStatusProc(StatusProcessamento status);

    @Query("SELECT t FROM TelemetriaRaw t JOIN FETCH t.satelite WHERE t.id = :id")
    Optional<TelemetriaRaw> findByIdComSatelite(Long id);
}
