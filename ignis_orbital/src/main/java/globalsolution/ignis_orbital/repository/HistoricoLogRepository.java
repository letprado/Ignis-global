package globalsolution.ignis_orbital.repository;

import globalsolution.ignis_orbital.entity.HistoricoLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoLogRepository extends JpaRepository<HistoricoLog, Long> {
    List<HistoricoLog> findByAlertaIdOrderByDtRegistroDesc(Long alertaId);
}
