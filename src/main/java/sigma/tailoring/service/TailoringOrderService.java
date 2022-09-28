package sigma.tailoring.service;

import sigma.tailoring.dto.ModifyTailoringOrder;
import sigma.tailoring.dto.TailoringOrderList;
import sigma.tailoring.tools.OrderSearchCriteria;
import sigma.tailoring.tools.Page;

public interface TailoringOrderService {
    TailoringOrderList findBy(OrderSearchCriteria orderSearchCriteria, Page page);

    boolean save(ModifyTailoringOrder order);

    boolean update(ModifyTailoringOrder order);
}
