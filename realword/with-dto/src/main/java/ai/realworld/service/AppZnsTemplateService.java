package ai.realworld.service;

import ai.realworld.domain.AppZnsTemplate;
import ai.realworld.repository.AppZnsTemplateRepository;
import ai.realworld.service.dto.AppZnsTemplateDTO;
import ai.realworld.service.mapper.AppZnsTemplateMapper;
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

    private final AppZnsTemplateMapper appZnsTemplateMapper;

    public AppZnsTemplateService(AppZnsTemplateRepository appZnsTemplateRepository, AppZnsTemplateMapper appZnsTemplateMapper) {
        this.appZnsTemplateRepository = appZnsTemplateRepository;
        this.appZnsTemplateMapper = appZnsTemplateMapper;
    }

    /**
     * Save a appZnsTemplate.
     *
     * @param appZnsTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    public AppZnsTemplateDTO save(AppZnsTemplateDTO appZnsTemplateDTO) {
        LOG.debug("Request to save AppZnsTemplate : {}", appZnsTemplateDTO);
        AppZnsTemplate appZnsTemplate = appZnsTemplateMapper.toEntity(appZnsTemplateDTO);
        appZnsTemplate = appZnsTemplateRepository.save(appZnsTemplate);
        return appZnsTemplateMapper.toDto(appZnsTemplate);
    }

    /**
     * Update a appZnsTemplate.
     *
     * @param appZnsTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    public AppZnsTemplateDTO update(AppZnsTemplateDTO appZnsTemplateDTO) {
        LOG.debug("Request to update AppZnsTemplate : {}", appZnsTemplateDTO);
        AppZnsTemplate appZnsTemplate = appZnsTemplateMapper.toEntity(appZnsTemplateDTO);
        appZnsTemplate = appZnsTemplateRepository.save(appZnsTemplate);
        return appZnsTemplateMapper.toDto(appZnsTemplate);
    }

    /**
     * Partially update a appZnsTemplate.
     *
     * @param appZnsTemplateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppZnsTemplateDTO> partialUpdate(AppZnsTemplateDTO appZnsTemplateDTO) {
        LOG.debug("Request to partially update AppZnsTemplate : {}", appZnsTemplateDTO);

        return appZnsTemplateRepository
            .findById(appZnsTemplateDTO.getId())
            .map(existingAppZnsTemplate -> {
                appZnsTemplateMapper.partialUpdate(existingAppZnsTemplate, appZnsTemplateDTO);

                return existingAppZnsTemplate;
            })
            .map(appZnsTemplateRepository::save)
            .map(appZnsTemplateMapper::toDto);
    }

    /**
     * Get one appZnsTemplate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppZnsTemplateDTO> findOne(Long id) {
        LOG.debug("Request to get AppZnsTemplate : {}", id);
        return appZnsTemplateRepository.findById(id).map(appZnsTemplateMapper::toDto);
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
