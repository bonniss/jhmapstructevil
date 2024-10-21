package ai.realworld.service;

import ai.realworld.domain.AppZnsTemplate;
import ai.realworld.repository.AppZnsTemplateRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AppZnsTemplate}.
 */
@Service
@Transactional
public class AppZnsTemplateService {

    private static final Logger LOG = LoggerFactory.getLogger(AppZnsTemplateService.class);

    private final AppZnsTemplateRepository appZnsTemplateRepository;

    public AppZnsTemplateService(AppZnsTemplateRepository appZnsTemplateRepository) {
        this.appZnsTemplateRepository = appZnsTemplateRepository;
    }

    /**
     * Save a appZnsTemplate.
     *
     * @param appZnsTemplate the entity to save.
     * @return the persisted entity.
     */
    public AppZnsTemplate save(AppZnsTemplate appZnsTemplate) {
        LOG.debug("Request to save AppZnsTemplate : {}", appZnsTemplate);
        return appZnsTemplateRepository.save(appZnsTemplate);
    }

    /**
     * Update a appZnsTemplate.
     *
     * @param appZnsTemplate the entity to save.
     * @return the persisted entity.
     */
    public AppZnsTemplate update(AppZnsTemplate appZnsTemplate) {
        LOG.debug("Request to update AppZnsTemplate : {}", appZnsTemplate);
        return appZnsTemplateRepository.save(appZnsTemplate);
    }

    /**
     * Partially update a appZnsTemplate.
     *
     * @param appZnsTemplate the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppZnsTemplate> partialUpdate(AppZnsTemplate appZnsTemplate) {
        LOG.debug("Request to partially update AppZnsTemplate : {}", appZnsTemplate);

        return appZnsTemplateRepository
            .findById(appZnsTemplate.getId())
            .map(existingAppZnsTemplate -> {
                if (appZnsTemplate.getZnsAction() != null) {
                    existingAppZnsTemplate.setZnsAction(appZnsTemplate.getZnsAction());
                }
                if (appZnsTemplate.getName() != null) {
                    existingAppZnsTemplate.setName(appZnsTemplate.getName());
                }
                if (appZnsTemplate.getTemplateId() != null) {
                    existingAppZnsTemplate.setTemplateId(appZnsTemplate.getTemplateId());
                }
                if (appZnsTemplate.getDataSourceMappingType() != null) {
                    existingAppZnsTemplate.setDataSourceMappingType(appZnsTemplate.getDataSourceMappingType());
                }
                if (appZnsTemplate.getTemplateDataMapping() != null) {
                    existingAppZnsTemplate.setTemplateDataMapping(appZnsTemplate.getTemplateDataMapping());
                }
                if (appZnsTemplate.getTargetUrls() != null) {
                    existingAppZnsTemplate.setTargetUrls(appZnsTemplate.getTargetUrls());
                }

                return existingAppZnsTemplate;
            })
            .map(appZnsTemplateRepository::save);
    }

    /**
     * Get one appZnsTemplate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppZnsTemplate> findOne(Long id) {
        LOG.debug("Request to get AppZnsTemplate : {}", id);
        return appZnsTemplateRepository.findById(id);
    }

    /**
     * Delete the appZnsTemplate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AppZnsTemplate : {}", id);
        appZnsTemplateRepository.deleteById(id);
    }
}
