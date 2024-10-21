package ai.realworld.service;

import ai.realworld.domain.AlBetonamuRelation;
import ai.realworld.repository.AlBetonamuRelationRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlBetonamuRelation}.
 */
@Service
@Transactional
public class AlBetonamuRelationService {

    private static final Logger LOG = LoggerFactory.getLogger(AlBetonamuRelationService.class);

    private final AlBetonamuRelationRepository alBetonamuRelationRepository;

    public AlBetonamuRelationService(AlBetonamuRelationRepository alBetonamuRelationRepository) {
        this.alBetonamuRelationRepository = alBetonamuRelationRepository;
    }

    /**
     * Save a alBetonamuRelation.
     *
     * @param alBetonamuRelation the entity to save.
     * @return the persisted entity.
     */
    public AlBetonamuRelation save(AlBetonamuRelation alBetonamuRelation) {
        LOG.debug("Request to save AlBetonamuRelation : {}", alBetonamuRelation);
        return alBetonamuRelationRepository.save(alBetonamuRelation);
    }

    /**
     * Update a alBetonamuRelation.
     *
     * @param alBetonamuRelation the entity to save.
     * @return the persisted entity.
     */
    public AlBetonamuRelation update(AlBetonamuRelation alBetonamuRelation) {
        LOG.debug("Request to update AlBetonamuRelation : {}", alBetonamuRelation);
        return alBetonamuRelationRepository.save(alBetonamuRelation);
    }

    /**
     * Partially update a alBetonamuRelation.
     *
     * @param alBetonamuRelation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlBetonamuRelation> partialUpdate(AlBetonamuRelation alBetonamuRelation) {
        LOG.debug("Request to partially update AlBetonamuRelation : {}", alBetonamuRelation);

        return alBetonamuRelationRepository
            .findById(alBetonamuRelation.getId())
            .map(existingAlBetonamuRelation -> {
                if (alBetonamuRelation.getType() != null) {
                    existingAlBetonamuRelation.setType(alBetonamuRelation.getType());
                }

                return existingAlBetonamuRelation;
            })
            .map(alBetonamuRelationRepository::save);
    }

    /**
     * Get one alBetonamuRelation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlBetonamuRelation> findOne(Long id) {
        LOG.debug("Request to get AlBetonamuRelation : {}", id);
        return alBetonamuRelationRepository.findById(id);
    }

    /**
     * Delete the alBetonamuRelation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlBetonamuRelation : {}", id);
        alBetonamuRelationRepository.deleteById(id);
    }
}
