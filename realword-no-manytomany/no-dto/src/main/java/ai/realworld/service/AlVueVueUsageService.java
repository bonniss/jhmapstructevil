package ai.realworld.service;

import ai.realworld.domain.AlVueVueUsage;
import ai.realworld.repository.AlVueVueUsageRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlVueVueUsage}.
 */
@Service
@Transactional
public class AlVueVueUsageService {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueUsageService.class);

    private final AlVueVueUsageRepository alVueVueUsageRepository;

    public AlVueVueUsageService(AlVueVueUsageRepository alVueVueUsageRepository) {
        this.alVueVueUsageRepository = alVueVueUsageRepository;
    }

    /**
     * Save a alVueVueUsage.
     *
     * @param alVueVueUsage the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueUsage save(AlVueVueUsage alVueVueUsage) {
        LOG.debug("Request to save AlVueVueUsage : {}", alVueVueUsage);
        return alVueVueUsageRepository.save(alVueVueUsage);
    }

    /**
     * Update a alVueVueUsage.
     *
     * @param alVueVueUsage the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueUsage update(AlVueVueUsage alVueVueUsage) {
        LOG.debug("Request to update AlVueVueUsage : {}", alVueVueUsage);
        return alVueVueUsageRepository.save(alVueVueUsage);
    }

    /**
     * Partially update a alVueVueUsage.
     *
     * @param alVueVueUsage the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlVueVueUsage> partialUpdate(AlVueVueUsage alVueVueUsage) {
        LOG.debug("Request to partially update AlVueVueUsage : {}", alVueVueUsage);

        return alVueVueUsageRepository.findById(alVueVueUsage.getId()).map(alVueVueUsageRepository::save);
    }

    /**
     * Get one alVueVueUsage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlVueVueUsage> findOne(UUID id) {
        LOG.debug("Request to get AlVueVueUsage : {}", id);
        return alVueVueUsageRepository.findById(id);
    }

    /**
     * Delete the alVueVueUsage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlVueVueUsage : {}", id);
        alVueVueUsageRepository.deleteById(id);
    }
}
