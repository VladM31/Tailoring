package sigma.nure.tailoring.tailoring.service;

import sigma.nure.tailoring.tailoring.tools.TailoringTemplateForm;
import sigma.nure.tailoring.tailoring.entities.TailoringTemplate;
import sigma.nure.tailoring.tailoring.tools.Page;
import sigma.nure.tailoring.tailoring.tools.TailoringTemplateSearchCriteria;

import java.util.List;
import java.util.Set;

public interface TailoringTemplateService {
    List<TailoringTemplate> findBy(TailoringTemplateSearchCriteria criteria, Page page);

    Set<String> findAllTypeTemplate();

    boolean save(TailoringTemplateForm templateForm);

    boolean update(TailoringTemplateForm templateForm);
}
