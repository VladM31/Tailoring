package sigma.nure.tailoring.tailoring.repository;

import sigma.nure.tailoring.tailoring.entities.TailoringOrder;
import sigma.nure.tailoring.tailoring.tools.OrderSearchCriteria;
import sigma.nure.tailoring.tailoring.tools.Page;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    List<TailoringOrder> findBy(OrderSearchCriteria parameters, Page page);

    Optional<Long> save(TailoringOrder order);

    boolean update(TailoringOrder order);
}
