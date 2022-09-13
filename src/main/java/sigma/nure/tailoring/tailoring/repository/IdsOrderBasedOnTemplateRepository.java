package sigma.nure.tailoring.tailoring.repository;

import java.util.List;
import java.util.Map;

public interface IdsOrderBasedOnTemplateRepository {
    Map<Long, List<Long>> findIdsForTemplatesAndOrdersBasedOnTemplate();
}
