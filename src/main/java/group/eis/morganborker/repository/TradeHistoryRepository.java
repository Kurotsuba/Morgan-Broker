package group.eis.morganborker.repository;

import group.eis.morganborker.entity.TradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeHistoryRepository extends JpaRepository<TradeHistory, Long> {
    List<TradeHistory> findAllByBuyTraderID(Long buyerID);
    List<TradeHistory> findAllBySellTraderID(Long sellerID);
}
