package ai.realworld.service;

import ai.realworld.domain.AlSherMaleVi;
import ai.realworld.repository.AlSherMaleViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlSherMaleVi}.
 */
@Service
@Transactional
public class AlSherMaleViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlSherMaleViService.class);

    private final AlSherMaleViRepository alSherMaleViRepository;

    public AlSherMaleViService(AlSherMaleViRepository alSherMaleViRepository) {
        this.alSherMaleViRepository = alSherMaleViRepository;
    }

    /**
     * Save a alSherMaleVi.
     *
     * @param alSherMaleVi the entity to save.
     * @return the persisted entity.
     */
    public AlSherMaleVi save(AlSherMaleVi alSherMaleVi) {
        LOG.debug("Request to save AlSherMaleVi : {}", alSherMaleVi);
        return alSherMaleViRepository.save(alSherMaleVi);
    }

    /**
     * Update a alSherMaleVi.
     *
     * @param alSherMaleVi the entity to save.
     * @return the persisted entity.
     */
    public AlSherMaleVi update(AlSherMaleVi alSherMaleVi) {
        LOG.debug("Request to update AlSherMaleVi : {}", alSherMaleVi);
        return alSherMaleViRepository.save(alSherMaleVi);
    }

    /**
     * Partially update a alSherMaleVi.
     *
     * @param alSherMaleVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlSherMaleVi> partialUpdate(AlSherMaleVi alSherMaleVi) {
        LOG.debug("Request to partially update AlSherMaleVi : {}", alSherMaleVi);

        return alSherMaleViRepository
            .findById(alSherMaleVi.getId())
            .map(existingAlSherMaleVi -> {
                if (alSherMaleVi.getDataSourceMappingType() != null) {
                    existingAlSherMaleVi.setDataSourceMappingType(alSherMaleVi.getDataSourceMappingType());
                }
                if (alSherMaleVi.getKeyword() != null) {
                    existingAlSherMaleVi.setKeyword(alSherMaleVi.getKeyword());
                }
                if (alSherMaleVi.getProperty() != null) {
                    existingAlSherMaleVi.setProperty(alSherMaleVi.getProperty());
                }
                if (alSherMaleVi.getTitle() != null) {
                    existingAlSherMaleVi.setTitle(alSherMaleVi.getTitle());
                }

                return existingAlSherMaleVi;
            })
            .map(alSherMaleViRepository::save);
    }

    /**
     * Get one alSherMaleVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlSherMaleVi> findOne(Long id) {
        LOG.debug("Request to get AlSherMaleVi : {}", id);
        return alSherMaleViRepository.findById(id);
    }

    /**
     * Delete the alSherMaleVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlSherMaleVi : {}", id);
        alSherMaleViRepository.deleteById(id);
    }
}
