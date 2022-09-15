package sigma.nure.tailoring.tailoring.service;

import sigma.nure.tailoring.tailoring.entities.PopularTailoringTemplate;

import java.util.List;

public interface PopularTemplateService {
    List<PopularTailoringTemplate> getPopularTailoringTemplateWithLimit(Long limit);

    List<PopularTailoringTemplate> getPopularTailoringTemplate();
}
