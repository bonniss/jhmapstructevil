package ai.realworld.service;

import ai.realworld.domain.AlLadyGaga;
import ai.realworld.repository.AlLadyGagaRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlLadyGaga}.
 */
@Service
@Transactional
public class AlLadyGagaService {

    private static final Logger LOG = LoggerFactory.getLogger(AlLadyGagaService.class);

    private final AlLadyGagaRepository alLadyGagaRepository;

    public AlLadyGagaService(AlLadyGagaRepository alLadyGagaRepository) {
        this.alLadyGagaRepository = alLadyGagaRepository;
    }

    /**
     * Save a alLadyGaga.
     *
     * @param alLadyGaga the entity to save.
     * @return the persisted entity.
     */
    public AlLadyGaga save(AlLadyGaga alLadyGaga) {
        LOG.debug("Request to save AlLadyGaga : {}", alLadyGaga);
        return alLadyGagaRepository.save(alLadyGaga);
    }

    /**
     * Update a alLadyGaga.
     *
     * @param alLadyGaga the entity to save.
     * @return the persisted entity.
     */
    public AlLadyGaga update(AlLadyGaga alLadyGaga) {
        LOG.debug("Request to update AlLadyGaga : {}", alLadyGaga);
        return alLadyGagaRepository.save(alLadyGaga);
    }

    /**
     * Partially update a alLadyGaga.
     *
     * @param alLadyGaga the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlLadyGaga> partialUpdate(AlLadyGaga alLadyGaga) {
        LOG.debug("Request to partially update AlLadyGaga : {}", alLadyGaga);

        return alLadyGagaRepository
            .findById(alLadyGaga.getId())
            .map(existingAlLadyGaga -> {
                if (alLadyGaga.getName() != null) {
                    existingAlLadyGaga.setName(alLadyGaga.getName());
                }
                if (alLadyGaga.getDescriptionHeitiga() != null) {
                    existingAlLadyGaga.setDescriptionHeitiga(alLadyGaga.getDescriptionHeitiga());
                }

                return existingAlLadyGaga;
            })
            .map(alLadyGagaRepository::save);
    }

    /**
     * Get one alLadyGaga by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlLadyGaga> findOne(UUID id) {
        LOG.debug("Request to get AlLadyGaga : {}", id);
        return alLadyGagaRepository.findById(id);
    }

    /**
     * Delete the alLadyGaga by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlLadyGaga : {}", id);
        alLadyGagaRepository.deleteById(id);
    }
}
