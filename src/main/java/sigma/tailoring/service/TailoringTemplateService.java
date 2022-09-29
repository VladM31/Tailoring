package sigma.tailoring.service;

import sigma.tailoring.tools.TailoringTemplateForm;
import sigma.tailoring.entities.TailoringTemplate;
import sigma.tailoring.tools.Page;
import sigma.tailoring.tools.TailoringTemplateSearchCriteria;

import java.util.List;
import java.util.Set;

public interface TailoringTemplateService {
    List<TailoringTemplate> findBy(TailoringTemplateSearchCriteria criteria, Page page);

    Set<String> findAllTypeTemplate();

    boolean save(TailoringTemplateForm templateForm);

    boolean update(TailoringTemplateForm templateForm);
}
