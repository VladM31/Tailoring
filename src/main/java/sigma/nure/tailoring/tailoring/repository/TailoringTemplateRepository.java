package sigma.nure.tailoring.tailoring.repository;

import sigma.nure.tailoring.tailoring.entities.TailoringTemplateWithMaterialIds;
import sigma.nure.tailoring.tailoring.tools.Page;
import sigma.nure.tailoring.tailoring.tools.TailoringTemplateSearchCriteria;

import java.util.List;
import java.util.Set;

public interface TailoringTemplateRepository {
    List<TailoringTemplateWithMaterialIds> findBy(TailoringTemplateSearchCriteria criteria, Page page);

    Set<String> findAllTypeTemplate();

    boolean save(TailoringTemplateWithMaterialIds template);

    boolean update(TailoringTemplateWithMaterialIds template);
}
