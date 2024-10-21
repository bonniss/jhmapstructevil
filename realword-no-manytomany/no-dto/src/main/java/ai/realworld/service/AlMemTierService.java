package ai.realworld.service;

import ai.realworld.domain.AlMemTier;
import ai.realworld.repository.AlMemTierRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlMemTier}.
 */
@Service
@Transactional
public class AlMemTierService {

    private static final Logger LOG = LoggerFactory.getLogger(AlMemTierService.class);

    private final AlMemTierRepository alMemTierRepository;

    public AlMemTierService(AlMemTierRepository alMemTierRepository) {
        this.alMemTierRepository = alMemTierRepository;
    }

    /**
     * Save a alMemTier.
     *
     * @param alMemTier the entity to save.
     * @return the persisted entity.
     */
    public AlMemTier save(AlMemTier alMemTier) {
        LOG.debug("Request to save AlMemTier : {}", alMemTier);
        return alMemTierRepository.save(alMemTier);
    }

    /**
     * Update a alMemTier.
     *
     * @param alMemTier the entity to save.
     * @return the persisted entity.
     */
    public AlMemTier update(AlMemTier alMemTier) {
        LOG.debug("Request to update AlMemTier : {}", alMemTier);
        return alMemTierRepository.save(alMemTier);
    }

    /**
     * Partially update a alMemTier.
     *
     * @param alMemTier the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlMemTier> partialUpdate(AlMemTier alMemTier) {
        LOG.debug("Request to partially update AlMemTier : {}", alMemTier);

        return alMemTierRepository
            .findById(alMemTier.getId())
            .map(existingAlMemTier -> {
                if (alMemTier.getName() != null) {
                    existingAlMemTier.setName(alMemTier.getName());
                }
                if (alMemTier.getDescription() != null) {
                    existingAlMemTier.setDescription(alMemTier.getDescription());
                }
                if (alMemTier.getMinPoint() != null) {
                    existingAlMemTier.setMinPoint(alMemTier.getMinPoint());
                }

                return existingAlMemTier;
            })
            .map(alMemTierRepository::save);
    }

    /**
     * Get one alMemTier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlMemTier> findOne(Long id) {
        LOG.debug("Request to get AlMemTier : {}", id);
        return alMemTierRepository.findById(id);
    }

    /**
     * Delete the alMemTier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlMemTier : {}", id);
        alMemTierRepository.deleteById(id);
    }
}
