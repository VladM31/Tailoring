package sigma.nure.tailoring.tailoring.repository;

import org.springframework.lang.Nullable;
import sigma.nure.tailoring.tailoring.entities.ShortTailoringOrderData;
import sigma.nure.tailoring.tailoring.entities.TailoringOrder;
import sigma.nure.tailoring.tailoring.tools.OrderParameters;
import sigma.nure.tailoring.tailoring.tools.Page;

import java.util.List;

public interface OrderRepository {

    List<TailoringOrder> findBy(OrderParameters parameters, Page page);

    boolean save(TailoringOrder order, @Nullable Long templateId);

    boolean update(TailoringOrder order);

    List<ShortTailoringOrderData> findShortDataByUserId(Long userId);
}
