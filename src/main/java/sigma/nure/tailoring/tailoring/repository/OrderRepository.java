package sigma.nure.tailoring.tailoring.repository;

import org.springframework.lang.Nullable;
import sigma.nure.tailoring.tailoring.entities.TailoringOrder;
import sigma.nure.tailoring.tailoring.tools.OrderParameters;
import sigma.nure.tailoring.tailoring.tools.Page;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    List<TailoringOrder> findBy(OrderParameters parameters, Page page);

    Optional<Long> saveAndReturnOrderId(TailoringOrder order);

    boolean pinToTemplate(Long orderId,Long templateId);

    boolean update(TailoringOrder order);
}
