package ai.realworld.service;

import ai.realworld.domain.HandCraft;
import ai.realworld.repository.HandCraftRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.HandCraft}.
 */
@Service
@Transactional
public class HandCraftService {

    private static final Logger LOG = LoggerFactory.getLogger(HandCraftService.class);

    private final HandCraftRepository handCraftRepository;

    public HandCraftService(HandCraftRepository handCraftRepository) {
        this.handCraftRepository = handCraftRepository;
    }

    /**
     * Save a handCraft.
     *
     * @param handCraft the entity to save.
     * @return the persisted entity.
     */
    public HandCraft save(HandCraft handCraft) {
        LOG.debug("Request to save HandCraft : {}", handCraft);
        return handCraftRepository.save(handCraft);
    }

    /**
     * Update a handCraft.
     *
     * @param handCraft the entity to save.
     * @return the persisted entity.
     */
    public HandCraft update(HandCraft handCraft) {
        LOG.debug("Request to update HandCraft : {}", handCraft);
        return handCraftRepository.save(handCraft);
    }

    /**
     * Partially update a handCraft.
     *
     * @param handCraft the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HandCraft> partialUpdate(HandCraft handCraft) {
        LOG.debug("Request to partially update HandCraft : {}", handCraft);

        return handCraftRepository.findById(handCraft.getId()).map(handCraftRepository::save);
    }

    /**
     * Get one handCraft by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HandCraft> findOne(Long id) {
        LOG.debug("Request to get HandCraft : {}", id);
        return handCraftRepository.findById(id);
    }

    /**
     * Delete the handCraft by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HandCraft : {}", id);
        handCraftRepository.deleteById(id);
    }
}
