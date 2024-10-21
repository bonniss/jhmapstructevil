package ai.realworld.service;

import ai.realworld.domain.AlZorroTemptationVi;
import ai.realworld.repository.AlZorroTemptationViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlZorroTemptationVi}.
 */
@Service
@Transactional
public class AlZorroTemptationViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlZorroTemptationViService.class);

    private final AlZorroTemptationViRepository alZorroTemptationViRepository;

    public AlZorroTemptationViService(AlZorroTemptationViRepository alZorroTemptationViRepository) {
        this.alZorroTemptationViRepository = alZorroTemptationViRepository;
    }

    /**
     * Save a alZorroTemptationVi.
     *
     * @param alZorroTemptationVi the entity to save.
     * @return the persisted entity.
     */
    public AlZorroTemptationVi save(AlZorroTemptationVi alZorroTemptationVi) {
        LOG.debug("Request to save AlZorroTemptationVi : {}", alZorroTemptationVi);
        return alZorroTemptationViRepository.save(alZorroTemptationVi);
    }

    /**
     * Update a alZorroTemptationVi.
     *
     * @param alZorroTemptationVi the entity to save.
     * @return the persisted entity.
     */
    public AlZorroTemptationVi update(AlZorroTemptationVi alZorroTemptationVi) {
        LOG.debug("Request to update AlZorroTemptationVi : {}", alZorroTemptationVi);
        return alZorroTemptationViRepository.save(alZorroTemptationVi);
    }

    /**
     * Partially update a alZorroTemptationVi.
     *
     * @param alZorroTemptationVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlZorroTemptationVi> partialUpdate(AlZorroTemptationVi alZorroTemptationVi) {
        LOG.debug("Request to partially update AlZorroTemptationVi : {}", alZorroTemptationVi);

        return alZorroTemptationViRepository
            .findById(alZorroTemptationVi.getId())
            .map(existingAlZorroTemptationVi -> {
                if (alZorroTemptationVi.getZipAction() != null) {
                    existingAlZorroTemptationVi.setZipAction(alZorroTemptationVi.getZipAction());
                }
                if (alZorroTemptationVi.getName() != null) {
                    existingAlZorroTemptationVi.setName(alZorroTemptationVi.getName());
                }
                if (alZorroTemptationVi.getTemplateId() != null) {
                    existingAlZorroTemptationVi.setTemplateId(alZorroTemptationVi.getTemplateId());
                }
                if (alZorroTemptationVi.getDataSourceMappingType() != null) {
                    existingAlZorroTemptationVi.setDataSourceMappingType(alZorroTemptationVi.getDataSourceMappingType());
                }
                if (alZorroTemptationVi.getTemplateDataMapping() != null) {
                    existingAlZorroTemptationVi.setTemplateDataMapping(alZorroTemptationVi.getTemplateDataMapping());
                }
                if (alZorroTemptationVi.getTargetUrls() != null) {
                    existingAlZorroTemptationVi.setTargetUrls(alZorroTemptationVi.getTargetUrls());
                }

                return existingAlZorroTemptationVi;
            })
            .map(alZorroTemptationViRepository::save);
    }

    /**
     * Get one alZorroTemptationVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlZorroTemptationVi> findOne(Long id) {
        LOG.debug("Request to get AlZorroTemptationVi : {}", id);
        return alZorroTemptationViRepository.findById(id);
    }

    /**
     * Delete the alZorroTemptationVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlZorroTemptationVi : {}", id);
        alZorroTemptationViRepository.deleteById(id);
    }
}
