package ai.realworld.service;

import ai.realworld.domain.AlBetonamuRelationVi;
import ai.realworld.repository.AlBetonamuRelationViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlBetonamuRelationVi}.
 */
@Service
@Transactional
public class AlBetonamuRelationViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlBetonamuRelationViService.class);

    private final AlBetonamuRelationViRepository alBetonamuRelationViRepository;

    public AlBetonamuRelationViService(AlBetonamuRelationViRepository alBetonamuRelationViRepository) {
        this.alBetonamuRelationViRepository = alBetonamuRelationViRepository;
    }

    /**
     * Save a alBetonamuRelationVi.
     *
     * @param alBetonamuRelationVi the entity to save.
     * @return the persisted entity.
     */
    public AlBetonamuRelationVi save(AlBetonamuRelationVi alBetonamuRelationVi) {
        LOG.debug("Request to save AlBetonamuRelationVi : {}", alBetonamuRelationVi);
        return alBetonamuRelationViRepository.save(alBetonamuRelationVi);
    }

    /**
     * Update a alBetonamuRelationVi.
     *
     * @param alBetonamuRelationVi the entity to save.
     * @return the persisted entity.
     */
    public AlBetonamuRelationVi update(AlBetonamuRelationVi alBetonamuRelationVi) {
        LOG.debug("Request to update AlBetonamuRelationVi : {}", alBetonamuRelationVi);
        return alBetonamuRelationViRepository.save(alBetonamuRelationVi);
    }

    /**
     * Partially update a alBetonamuRelationVi.
     *
     * @param alBetonamuRelationVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlBetonamuRelationVi> partialUpdate(AlBetonamuRelationVi alBetonamuRelationVi) {
        LOG.debug("Request to partially update AlBetonamuRelationVi : {}", alBetonamuRelationVi);

        return alBetonamuRelationViRepository
            .findById(alBetonamuRelationVi.getId())
            .map(existingAlBetonamuRelationVi -> {
                if (alBetonamuRelationVi.getType() != null) {
                    existingAlBetonamuRelationVi.setType(alBetonamuRelationVi.getType());
                }

                return existingAlBetonamuRelationVi;
            })
            .map(alBetonamuRelationViRepository::save);
    }

    /**
     * Get one alBetonamuRelationVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlBetonamuRelationVi> findOne(Long id) {
        LOG.debug("Request to get AlBetonamuRelationVi : {}", id);
        return alBetonamuRelationViRepository.findById(id);
    }

    /**
     * Delete the alBetonamuRelationVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlBetonamuRelationVi : {}", id);
        alBetonamuRelationViRepository.deleteById(id);
    }
}
