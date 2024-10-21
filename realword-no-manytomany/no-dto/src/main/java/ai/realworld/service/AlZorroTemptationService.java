package ai.realworld.service;

import ai.realworld.domain.AlZorroTemptation;
import ai.realworld.repository.AlZorroTemptationRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlZorroTemptation}.
 */
@Service
@Transactional
public class AlZorroTemptationService {

    private static final Logger LOG = LoggerFactory.getLogger(AlZorroTemptationService.class);

    private final AlZorroTemptationRepository alZorroTemptationRepository;

    public AlZorroTemptationService(AlZorroTemptationRepository alZorroTemptationRepository) {
        this.alZorroTemptationRepository = alZorroTemptationRepository;
    }

    /**
     * Save a alZorroTemptation.
     *
     * @param alZorroTemptation the entity to save.
     * @return the persisted entity.
     */
    public AlZorroTemptation save(AlZorroTemptation alZorroTemptation) {
        LOG.debug("Request to save AlZorroTemptation : {}", alZorroTemptation);
        return alZorroTemptationRepository.save(alZorroTemptation);
    }

    /**
     * Update a alZorroTemptation.
     *
     * @param alZorroTemptation the entity to save.
     * @return the persisted entity.
     */
    public AlZorroTemptation update(AlZorroTemptation alZorroTemptation) {
        LOG.debug("Request to update AlZorroTemptation : {}", alZorroTemptation);
        return alZorroTemptationRepository.save(alZorroTemptation);
    }

    /**
     * Partially update a alZorroTemptation.
     *
     * @param alZorroTemptation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlZorroTemptation> partialUpdate(AlZorroTemptation alZorroTemptation) {
        LOG.debug("Request to partially update AlZorroTemptation : {}", alZorroTemptation);

        return alZorroTemptationRepository
            .findById(alZorroTemptation.getId())
            .map(existingAlZorroTemptation -> {
                if (alZorroTemptation.getZipAction() != null) {
                    existingAlZorroTemptation.setZipAction(alZorroTemptation.getZipAction());
                }
                if (alZorroTemptation.getName() != null) {
                    existingAlZorroTemptation.setName(alZorroTemptation.getName());
                }
                if (alZorroTemptation.getTemplateId() != null) {
                    existingAlZorroTemptation.setTemplateId(alZorroTemptation.getTemplateId());
                }
                if (alZorroTemptation.getDataSourceMappingType() != null) {
                    existingAlZorroTemptation.setDataSourceMappingType(alZorroTemptation.getDataSourceMappingType());
                }
                if (alZorroTemptation.getTemplateDataMapping() != null) {
                    existingAlZorroTemptation.setTemplateDataMapping(alZorroTemptation.getTemplateDataMapping());
                }
                if (alZorroTemptation.getTargetUrls() != null) {
                    existingAlZorroTemptation.setTargetUrls(alZorroTemptation.getTargetUrls());
                }

                return existingAlZorroTemptation;
            })
            .map(alZorroTemptationRepository::save);
    }

    /**
     * Get one alZorroTemptation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlZorroTemptation> findOne(Long id) {
        LOG.debug("Request to get AlZorroTemptation : {}", id);
        return alZorroTemptationRepository.findById(id);
    }

    /**
     * Delete the alZorroTemptation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlZorroTemptation : {}", id);
        alZorroTemptationRepository.deleteById(id);
    }
}
