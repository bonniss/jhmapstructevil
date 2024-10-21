package ai.realworld.service;

import ai.realworld.domain.AlLadyGagaVi;
import ai.realworld.repository.AlLadyGagaViRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlLadyGagaVi}.
 */
@Service
@Transactional
public class AlLadyGagaViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlLadyGagaViService.class);

    private final AlLadyGagaViRepository alLadyGagaViRepository;

    public AlLadyGagaViService(AlLadyGagaViRepository alLadyGagaViRepository) {
        this.alLadyGagaViRepository = alLadyGagaViRepository;
    }

    /**
     * Save a alLadyGagaVi.
     *
     * @param alLadyGagaVi the entity to save.
     * @return the persisted entity.
     */
    public AlLadyGagaVi save(AlLadyGagaVi alLadyGagaVi) {
        LOG.debug("Request to save AlLadyGagaVi : {}", alLadyGagaVi);
        return alLadyGagaViRepository.save(alLadyGagaVi);
    }

    /**
     * Update a alLadyGagaVi.
     *
     * @param alLadyGagaVi the entity to save.
     * @return the persisted entity.
     */
    public AlLadyGagaVi update(AlLadyGagaVi alLadyGagaVi) {
        LOG.debug("Request to update AlLadyGagaVi : {}", alLadyGagaVi);
        return alLadyGagaViRepository.save(alLadyGagaVi);
    }

    /**
     * Partially update a alLadyGagaVi.
     *
     * @param alLadyGagaVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlLadyGagaVi> partialUpdate(AlLadyGagaVi alLadyGagaVi) {
        LOG.debug("Request to partially update AlLadyGagaVi : {}", alLadyGagaVi);

        return alLadyGagaViRepository
            .findById(alLadyGagaVi.getId())
            .map(existingAlLadyGagaVi -> {
                if (alLadyGagaVi.getName() != null) {
                    existingAlLadyGagaVi.setName(alLadyGagaVi.getName());
                }
                if (alLadyGagaVi.getDescriptionHeitiga() != null) {
                    existingAlLadyGagaVi.setDescriptionHeitiga(alLadyGagaVi.getDescriptionHeitiga());
                }

                return existingAlLadyGagaVi;
            })
            .map(alLadyGagaViRepository::save);
    }

    /**
     * Get one alLadyGagaVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlLadyGagaVi> findOne(UUID id) {
        LOG.debug("Request to get AlLadyGagaVi : {}", id);
        return alLadyGagaViRepository.findById(id);
    }

    /**
     * Delete the alLadyGagaVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlLadyGagaVi : {}", id);
        alLadyGagaViRepository.deleteById(id);
    }
}
