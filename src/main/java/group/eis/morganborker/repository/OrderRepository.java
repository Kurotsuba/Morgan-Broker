package group.eis.morganborker.repository;

import group.eis.morganborker.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findOrderByTraderIDAndTraderOrderID(Long traderID, Long traderOrderID);
    List<Order> findAllByFutureID(Long futureID);
}
