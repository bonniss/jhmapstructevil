package ai.realworld.service;

import ai.realworld.domain.AlProtyVi;
import ai.realworld.repository.AlProtyViRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlProtyVi}.
 */
@Service
@Transactional
public class AlProtyViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlProtyViService.class);

    private final AlProtyViRepository alProtyViRepository;

    public AlProtyViService(AlProtyViRepository alProtyViRepository) {
        this.alProtyViRepository = alProtyViRepository;
    }

    /**
     * Save a alProtyVi.
     *
     * @param alProtyVi the entity to save.
     * @return the persisted entity.
     */
    public AlProtyVi save(AlProtyVi alProtyVi) {
        LOG.debug("Request to save AlProtyVi : {}", alProtyVi);
        return alProtyViRepository.save(alProtyVi);
    }

    /**
     * Update a alProtyVi.
     *
     * @param alProtyVi the entity to save.
     * @return the persisted entity.
     */
    public AlProtyVi update(AlProtyVi alProtyVi) {
        LOG.debug("Request to update AlProtyVi : {}", alProtyVi);
        return alProtyViRepository.save(alProtyVi);
    }

    /**
     * Partially update a alProtyVi.
     *
     * @param alProtyVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlProtyVi> partialUpdate(AlProtyVi alProtyVi) {
        LOG.debug("Request to partially update AlProtyVi : {}", alProtyVi);

        return alProtyViRepository
            .findById(alProtyVi.getId())
            .map(existingAlProtyVi -> {
                if (alProtyVi.getName() != null) {
                    existingAlProtyVi.setName(alProtyVi.getName());
                }
                if (alProtyVi.getDescriptionHeitiga() != null) {
                    existingAlProtyVi.setDescriptionHeitiga(alProtyVi.getDescriptionHeitiga());
                }
                if (alProtyVi.getCoordinate() != null) {
                    existingAlProtyVi.setCoordinate(alProtyVi.getCoordinate());
                }
                if (alProtyVi.getCode() != null) {
                    existingAlProtyVi.setCode(alProtyVi.getCode());
                }
                if (alProtyVi.getStatus() != null) {
                    existingAlProtyVi.setStatus(alProtyVi.getStatus());
                }
                if (alProtyVi.getIsEnabled() != null) {
                    existingAlProtyVi.setIsEnabled(alProtyVi.getIsEnabled());
                }

                return existingAlProtyVi;
            })
            .map(alProtyViRepository::save);
    }

    /**
     * Get one alProtyVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlProtyVi> findOne(UUID id) {
        LOG.debug("Request to get AlProtyVi : {}", id);
        return alProtyViRepository.findById(id);
    }

    /**
     * Delete the alProtyVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlProtyVi : {}", id);
        alProtyViRepository.deleteById(id);
    }
}
