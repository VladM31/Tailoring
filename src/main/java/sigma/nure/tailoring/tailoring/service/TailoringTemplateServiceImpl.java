package sigma.nure.tailoring.tailoring.service;

import com.google.gson.Gson;
import sigma.nure.tailoring.tailoring.converters.TailoringTemplateConvertor;
import sigma.nure.tailoring.tailoring.converters.TailoringTemplateSortColumnConverter;
import sigma.nure.tailoring.tailoring.repository.MaterialsRepository;
import sigma.nure.tailoring.tailoring.repository.TailoringTemplateRepository;
import sigma.nure.tailoring.tailoring.converters.FileConverter;
import sigma.nure.tailoring.tailoring.tools.TailoringTemplateForm;
import sigma.nure.tailoring.tailoring.entities.TailoringTemplate;
import sigma.nure.tailoring.tailoring.entities.TailoringTemplateWithMaterialIds;
import sigma.nure.tailoring.tailoring.tools.Page;
import sigma.nure.tailoring.tailoring.tools.TailoringTemplateSearchCriteria;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TailoringTemplateServiceImpl implements TailoringTemplateService {
    private final Gson gson;
    private final String imagesDirectory;
    private final FileConverter fileConverter;
    private final TailoringTemplateRepository templateRepository;
    private final TailoringTemplateSortColumnConverter tailoringTemplateSortColumnConverter;
    private final TailoringTemplateConvertor tailoringTemplateConvertor;

    public TailoringTemplateServiceImpl(FileConverter fileConverter, TailoringTemplateRepository templateRepository,
                                        String imagesDirectory, TailoringTemplateSortColumnConverter tailoringTemplateSortColumnConverter,
                                        TailoringTemplateConvertor tailoringTemplateConvertor) {
        this.fileConverter = fileConverter;
        this.templateRepository = templateRepository;
        this.imagesDirectory = imagesDirectory;
        this.tailoringTemplateSortColumnConverter = tailoringTemplateSortColumnConverter;
        this.tailoringTemplateConvertor = tailoringTemplateConvertor;
        this.gson = new Gson();
    }

    @Override
    public List<TailoringTemplate> findBy(TailoringTemplateSearchCriteria criteria, Page page) {
        page.setOrderBy(tailoringTemplateSortColumnConverter.convert(page.getOrderBy()));

        return templateRepository
                .findBy(criteria, page)
                .stream()
                .map(tailoringTemplateConvertor
                        .getConverter()::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Set<String> findAllTypeTemplate() {
        return templateRepository.findAllTypeTemplate();
    }

    @Override
    public boolean save(TailoringTemplateForm templateForm) {
        List<File> files = fileConverter.toFiles(imagesDirectory, List.of(templateForm.getUploadImages()));
        templateForm.setImagesUrl(files.toArray(File[]::new));

        TailoringTemplateWithMaterialIds template = toTailoringTemplateWithMaterialIds(templateForm);
        template.setDateOfCreation(LocalDateTime.now());

        return templateRepository.save(template);
    }

    @Override
    public boolean update(TailoringTemplateForm templateForm) {
        if (templateForm.getUploadImages().length != 0) {
            List<File> files = fileConverter.toFiles(imagesDirectory, List.of(templateForm.getUploadImages()));
            templateForm.setImagesUrl(files.toArray(File[]::new));
        }

        TailoringTemplateWithMaterialIds template = toTailoringTemplateWithMaterialIds(templateForm);
        template.setDateOfCreation(templateForm.getDateOfCreation());
        setMaterialsIfNotExist(template);

        return templateRepository.update(template);
    }

    private TailoringTemplateWithMaterialIds toTailoringTemplateWithMaterialIds(TailoringTemplateForm form) {
        TailoringTemplateWithMaterialIds template = new TailoringTemplateWithMaterialIds();

        template.setId(form.getId());
        template.setName(form.getName());
        template.setActive(form.isActive());
        template.setCost(form.getCost());
        template.setTypeTemplate(form.getTypeTemplate());
        template.setTemplateDescription(form.getTemplateDescription());
        template.setMaterialIds(getEmptySetIfNull(form.getMaterialIds()));
        template.setColorIds(getEmptySetIfNull(form.getColorIds()));
        template.setImagesUrl(
                Arrays.stream(form.getImagesUrl())
                        .map(f -> f.getName())
                        .collect(Collectors.toSet()));
        template.setPartSizeForTemplates(form.toPartSizeForTemplates(gson));

        return template;
    }

    private Set<Integer> getEmptySetIfNull(Integer[] array) {
        return array == null ? Set.of() : Set.of(array);
    }


    private void setMaterialsIfNotExist(TailoringTemplateWithMaterialIds newTemplate) {
        if (newTemplate.getMaterialIds().size() != 0 && newTemplate.getColorIds().size() != 0) {
            return;
        }

        TailoringTemplateWithMaterialIds oldTemplate = templateRepository.findBy(
                        TailoringTemplateSearchCriteria.builder().templateIds(List.of(newTemplate.getId())).build(), new Page())
                .stream().findFirst().orElseThrow();

        if (newTemplate.getMaterialIds().isEmpty()) {
            newTemplate.setMaterialIds(oldTemplate.getMaterialIds());
        }

        if (newTemplate.getColorIds().isEmpty()) {
            newTemplate.setColorIds(oldTemplate.getColorIds());
        }
    }
}
