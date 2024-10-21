package ai.realworld.service;

import ai.realworld.domain.AlProty;
import ai.realworld.repository.AlProtyRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlProty}.
 */
@Service
@Transactional
public class AlProtyService {

    private static final Logger LOG = LoggerFactory.getLogger(AlProtyService.class);

    private final AlProtyRepository alProtyRepository;

    public AlProtyService(AlProtyRepository alProtyRepository) {
        this.alProtyRepository = alProtyRepository;
    }

    /**
     * Save a alProty.
     *
     * @param alProty the entity to save.
     * @return the persisted entity.
     */
    public AlProty save(AlProty alProty) {
        LOG.debug("Request to save AlProty : {}", alProty);
        return alProtyRepository.save(alProty);
    }

    /**
     * Update a alProty.
     *
     * @param alProty the entity to save.
     * @return the persisted entity.
     */
    public AlProty update(AlProty alProty) {
        LOG.debug("Request to update AlProty : {}", alProty);
        return alProtyRepository.save(alProty);
    }

    /**
     * Partially update a alProty.
     *
     * @param alProty the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlProty> partialUpdate(AlProty alProty) {
        LOG.debug("Request to partially update AlProty : {}", alProty);

        return alProtyRepository
            .findById(alProty.getId())
            .map(existingAlProty -> {
                if (alProty.getName() != null) {
                    existingAlProty.setName(alProty.getName());
                }
                if (alProty.getDescriptionHeitiga() != null) {
                    existingAlProty.setDescriptionHeitiga(alProty.getDescriptionHeitiga());
                }
                if (alProty.getCoordinate() != null) {
                    existingAlProty.setCoordinate(alProty.getCoordinate());
                }
                if (alProty.getCode() != null) {
                    existingAlProty.setCode(alProty.getCode());
                }
                if (alProty.getStatus() != null) {
                    existingAlProty.setStatus(alProty.getStatus());
                }
                if (alProty.getIsEnabled() != null) {
                    existingAlProty.setIsEnabled(alProty.getIsEnabled());
                }

                return existingAlProty;
            })
            .map(alProtyRepository::save);
    }

    /**
     * Get one alProty by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlProty> findOne(UUID id) {
        LOG.debug("Request to get AlProty : {}", id);
        return alProtyRepository.findById(id);
    }

    /**
     * Delete the alProty by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlProty : {}", id);
        alProtyRepository.deleteById(id);
    }
}
