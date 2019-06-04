package group.eis.morganborker.repository;

import group.eis.morganborker.entity.TradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<TradeHistory, Long> {
}
