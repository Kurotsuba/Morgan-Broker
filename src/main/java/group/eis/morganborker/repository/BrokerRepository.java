package group.eis.morganborker.repository;

import group.eis.morganborker.entity.Broker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrokerRepository extends JpaRepository<Broker, Long> {
}
