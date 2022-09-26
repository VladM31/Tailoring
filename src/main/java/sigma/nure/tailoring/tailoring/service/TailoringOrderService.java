package sigma.nure.tailoring.tailoring.service;

import sigma.nure.tailoring.tailoring.dto.ModifyTailoringOrder;
import sigma.nure.tailoring.tailoring.dto.TailoringOrderList;
import sigma.nure.tailoring.tailoring.tools.OrderSearchCriteria;
import sigma.nure.tailoring.tailoring.tools.Page;

public interface TailoringOrderService {
    TailoringOrderList findBy(OrderSearchCriteria orderSearchCriteria, Page page);

    boolean save(ModifyTailoringOrder order);

    boolean update(ModifyTailoringOrder order);
}
