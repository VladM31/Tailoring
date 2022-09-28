package sigma.tailoring.repository;

import sigma.tailoring.entities.TailoringTemplateWithMaterialIds;
import sigma.tailoring.tools.Page;
import sigma.tailoring.tools.TailoringTemplateSearchCriteria;

import java.util.List;
import java.util.Set;

public interface TailoringTemplateRepository {
    List<TailoringTemplateWithMaterialIds> findBy(TailoringTemplateSearchCriteria criteria, Page page);

    Set<String> findAllTypeTemplate();

    boolean save(TailoringTemplateWithMaterialIds template);

    boolean update(TailoringTemplateWithMaterialIds template);
}
