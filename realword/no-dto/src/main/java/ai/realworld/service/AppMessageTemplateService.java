package ai.realworld.service;

import ai.realworld.domain.AppMessageTemplate;
import ai.realworld.repository.AppMessageTemplateRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AppMessageTemplate}.
 */
@Service
@Transactional
public class AppMessageTemplateService {

    private static final Logger LOG = LoggerFactory.getLogger(AppMessageTemplateService.class);

    private final AppMessageTemplateRepository appMessageTemplateRepository;

    public AppMessageTemplateService(AppMessageTemplateRepository appMessageTemplateRepository) {
        this.appMessageTemplateRepository = appMessageTemplateRepository;
    }

    /**
     * Save a appMessageTemplate.
     *
     * @param appMessageTemplate the entity to save.
     * @return the persisted entity.
     */
    public AppMessageTemplate save(AppMessageTemplate appMessageTemplate) {
        LOG.debug("Request to save AppMessageTemplate : {}", appMessageTemplate);
        return appMessageTemplateRepository.save(appMessageTemplate);
    }

    /**
     * Update a appMessageTemplate.
     *
     * @param appMessageTemplate the entity to save.
     * @return the persisted entity.
     */
    public AppMessageTemplate update(AppMessageTemplate appMessageTemplate) {
        LOG.debug("Request to update AppMessageTemplate : {}", appMessageTemplate);
        return appMessageTemplateRepository.save(appMessageTemplate);
    }

    /**
     * Partially update a appMessageTemplate.
     *
     * @param appMessageTemplate the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppMessageTemplate> partialUpdate(AppMessageTemplate appMessageTemplate) {
        LOG.debug("Request to partially update AppMessageTemplate : {}", appMessageTemplate);

        return appMessageTemplateRepository
            .findById(appMessageTemplate.getId())
            .map(existingAppMessageTemplate -> {
                if (appMessageTemplate.getTitle() != null) {
                    existingAppMessageTemplate.setTitle(appMessageTemplate.getTitle());
                }
                if (appMessageTemplate.getTopContent() != null) {
                    existingAppMessageTemplate.setTopContent(appMessageTemplate.getTopContent());
                }
                if (appMessageTemplate.getContent() != null) {
                    existingAppMessageTemplate.setContent(appMessageTemplate.getContent());
                }
                if (appMessageTemplate.getBottomContent() != null) {
                    existingAppMessageTemplate.setBottomContent(appMessageTemplate.getBottomContent());
                }
                if (appMessageTemplate.getPropTitleMappingJason() != null) {
                    existingAppMessageTemplate.setPropTitleMappingJason(appMessageTemplate.getPropTitleMappingJason());
                }
                if (appMessageTemplate.getDataSourceMappingType() != null) {
                    existingAppMessageTemplate.setDataSourceMappingType(appMessageTemplate.getDataSourceMappingType());
                }
                if (appMessageTemplate.getTargetUrls() != null) {
                    existingAppMessageTemplate.setTargetUrls(appMessageTemplate.getTargetUrls());
                }

                return existingAppMessageTemplate;
            })
            .map(appMessageTemplateRepository::save);
    }

    /**
     * Get one appMessageTemplate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppMessageTemplate> findOne(Long id) {
        LOG.debug("Request to get AppMessageTemplate : {}", id);
        return appMessageTemplateRepository.findById(id);
    }

    /**
     * Delete the appMessageTemplate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AppMessageTemplate : {}", id);
        appMessageTemplateRepository.deleteById(id);
    }
}
