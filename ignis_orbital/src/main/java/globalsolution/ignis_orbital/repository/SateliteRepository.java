package globalsolution.ignis_orbital.repository;

import globalsolution.ignis_orbital.entity.Satelite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SateliteRepository extends JpaRepository<Satelite, Long> {
    Optional<Satelite> findByNmSatelite(String nmSatelite);
}
