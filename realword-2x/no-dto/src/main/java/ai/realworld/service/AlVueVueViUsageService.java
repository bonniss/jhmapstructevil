package ai.realworld.service;

import ai.realworld.domain.AlVueVueViUsage;
import ai.realworld.repository.AlVueVueViUsageRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlVueVueViUsage}.
 */
@Service
@Transactional
public class AlVueVueViUsageService {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueViUsageService.class);

    private final AlVueVueViUsageRepository alVueVueViUsageRepository;

    public AlVueVueViUsageService(AlVueVueViUsageRepository alVueVueViUsageRepository) {
        this.alVueVueViUsageRepository = alVueVueViUsageRepository;
    }

    /**
     * Save a alVueVueViUsage.
     *
     * @param alVueVueViUsage the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueViUsage save(AlVueVueViUsage alVueVueViUsage) {
        LOG.debug("Request to save AlVueVueViUsage : {}", alVueVueViUsage);
        return alVueVueViUsageRepository.save(alVueVueViUsage);
    }

    /**
     * Update a alVueVueViUsage.
     *
     * @param alVueVueViUsage the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueViUsage update(AlVueVueViUsage alVueVueViUsage) {
        LOG.debug("Request to update AlVueVueViUsage : {}", alVueVueViUsage);
        return alVueVueViUsageRepository.save(alVueVueViUsage);
    }

    /**
     * Partially update a alVueVueViUsage.
     *
     * @param alVueVueViUsage the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlVueVueViUsage> partialUpdate(AlVueVueViUsage alVueVueViUsage) {
        LOG.debug("Request to partially update AlVueVueViUsage : {}", alVueVueViUsage);

        return alVueVueViUsageRepository.findById(alVueVueViUsage.getId()).map(alVueVueViUsageRepository::save);
    }

    /**
     * Get one alVueVueViUsage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlVueVueViUsage> findOne(UUID id) {
        LOG.debug("Request to get AlVueVueViUsage : {}", id);
        return alVueVueViUsageRepository.findById(id);
    }

    /**
     * Delete the alVueVueViUsage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlVueVueViUsage : {}", id);
        alVueVueViUsageRepository.deleteById(id);
    }
}
