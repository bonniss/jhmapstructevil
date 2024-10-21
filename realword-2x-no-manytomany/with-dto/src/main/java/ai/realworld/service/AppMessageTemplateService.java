package ai.realworld.service;

import ai.realworld.domain.AppMessageTemplate;
import ai.realworld.repository.AppMessageTemplateRepository;
import ai.realworld.service.dto.AppMessageTemplateDTO;
import ai.realworld.service.mapper.AppMessageTemplateMapper;
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

    private final AppMessageTemplateMapper appMessageTemplateMapper;

    public AppMessageTemplateService(
        AppMessageTemplateRepository appMessageTemplateRepository,
        AppMessageTemplateMapper appMessageTemplateMapper
    ) {
        this.appMessageTemplateRepository = appMessageTemplateRepository;
        this.appMessageTemplateMapper = appMessageTemplateMapper;
    }

    /**
     * Save a appMessageTemplate.
     *
     * @param appMessageTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    public AppMessageTemplateDTO save(AppMessageTemplateDTO appMessageTemplateDTO) {
        LOG.debug("Request to save AppMessageTemplate : {}", appMessageTemplateDTO);
        AppMessageTemplate appMessageTemplate = appMessageTemplateMapper.toEntity(appMessageTemplateDTO);
        appMessageTemplate = appMessageTemplateRepository.save(appMessageTemplate);
        return appMessageTemplateMapper.toDto(appMessageTemplate);
    }

    /**
     * Update a appMessageTemplate.
     *
     * @param appMessageTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    public AppMessageTemplateDTO update(AppMessageTemplateDTO appMessageTemplateDTO) {
        LOG.debug("Request to update AppMessageTemplate : {}", appMessageTemplateDTO);
        AppMessageTemplate appMessageTemplate = appMessageTemplateMapper.toEntity(appMessageTemplateDTO);
        appMessageTemplate = appMessageTemplateRepository.save(appMessageTemplate);
        return appMessageTemplateMapper.toDto(appMessageTemplate);
    }

    /**
     * Partially update a appMessageTemplate.
     *
     * @param appMessageTemplateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppMessageTemplateDTO> partialUpdate(AppMessageTemplateDTO appMessageTemplateDTO) {
        LOG.debug("Request to partially update AppMessageTemplate : {}", appMessageTemplateDTO);

        return appMessageTemplateRepository
            .findById(appMessageTemplateDTO.getId())
            .map(existingAppMessageTemplate -> {
                appMessageTemplateMapper.partialUpdate(existingAppMessageTemplate, appMessageTemplateDTO);

                return existingAppMessageTemplate;
            })
            .map(appMessageTemplateRepository::save)
            .map(appMessageTemplateMapper::toDto);
    }

    /**
     * Get one appMessageTemplate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppMessageTemplateDTO> findOne(Long id) {
        LOG.debug("Request to get AppMessageTemplate : {}", id);
        return appMessageTemplateRepository.findById(id).map(appMessageTemplateMapper::toDto);
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
