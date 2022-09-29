package sigma.tailoring.repository;

import sigma.tailoring.entities.TailoringOrder;
import sigma.tailoring.tools.OrderSearchCriteria;
import sigma.tailoring.tools.Page;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    List<TailoringOrder> findBy(OrderSearchCriteria parameters, Page page);

    Optional<Long> save(TailoringOrder order);

    boolean update(TailoringOrder order);
}
