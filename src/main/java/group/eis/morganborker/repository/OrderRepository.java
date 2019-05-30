package group.eis.morganborker.repository;

import group.eis.morganborker.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
