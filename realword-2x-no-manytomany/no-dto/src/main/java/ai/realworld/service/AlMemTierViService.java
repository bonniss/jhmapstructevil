package ai.realworld.service;

import ai.realworld.domain.AlMemTierVi;
import ai.realworld.repository.AlMemTierViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlMemTierVi}.
 */
@Service
@Transactional
public class AlMemTierViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlMemTierViService.class);

    private final AlMemTierViRepository alMemTierViRepository;

    public AlMemTierViService(AlMemTierViRepository alMemTierViRepository) {
        this.alMemTierViRepository = alMemTierViRepository;
    }

    /**
     * Save a alMemTierVi.
     *
     * @param alMemTierVi the entity to save.
     * @return the persisted entity.
     */
    public AlMemTierVi save(AlMemTierVi alMemTierVi) {
        LOG.debug("Request to save AlMemTierVi : {}", alMemTierVi);
        return alMemTierViRepository.save(alMemTierVi);
    }

    /**
     * Update a alMemTierVi.
     *
     * @param alMemTierVi the entity to save.
     * @return the persisted entity.
     */
    public AlMemTierVi update(AlMemTierVi alMemTierVi) {
        LOG.debug("Request to update AlMemTierVi : {}", alMemTierVi);
        return alMemTierViRepository.save(alMemTierVi);
    }

    /**
     * Partially update a alMemTierVi.
     *
     * @param alMemTierVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlMemTierVi> partialUpdate(AlMemTierVi alMemTierVi) {
        LOG.debug("Request to partially update AlMemTierVi : {}", alMemTierVi);

        return alMemTierViRepository
            .findById(alMemTierVi.getId())
            .map(existingAlMemTierVi -> {
                if (alMemTierVi.getName() != null) {
                    existingAlMemTierVi.setName(alMemTierVi.getName());
                }
                if (alMemTierVi.getDescription() != null) {
                    existingAlMemTierVi.setDescription(alMemTierVi.getDescription());
                }
                if (alMemTierVi.getMinPoint() != null) {
                    existingAlMemTierVi.setMinPoint(alMemTierVi.getMinPoint());
                }

                return existingAlMemTierVi;
            })
            .map(alMemTierViRepository::save);
    }

    /**
     * Get one alMemTierVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlMemTierVi> findOne(Long id) {
        LOG.debug("Request to get AlMemTierVi : {}", id);
        return alMemTierViRepository.findById(id);
    }

    /**
     * Delete the alMemTierVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlMemTierVi : {}", id);
        alMemTierViRepository.deleteById(id);
    }
}
