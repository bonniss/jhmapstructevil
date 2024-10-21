package ai.realworld.service;

import ai.realworld.domain.HandCraftVi;
import ai.realworld.repository.HandCraftViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.HandCraftVi}.
 */
@Service
@Transactional
public class HandCraftViService {

    private static final Logger LOG = LoggerFactory.getLogger(HandCraftViService.class);

    private final HandCraftViRepository handCraftViRepository;

    public HandCraftViService(HandCraftViRepository handCraftViRepository) {
        this.handCraftViRepository = handCraftViRepository;
    }

    /**
     * Save a handCraftVi.
     *
     * @param handCraftVi the entity to save.
     * @return the persisted entity.
     */
    public HandCraftVi save(HandCraftVi handCraftVi) {
        LOG.debug("Request to save HandCraftVi : {}", handCraftVi);
        return handCraftViRepository.save(handCraftVi);
    }

    /**
     * Update a handCraftVi.
     *
     * @param handCraftVi the entity to save.
     * @return the persisted entity.
     */
    public HandCraftVi update(HandCraftVi handCraftVi) {
        LOG.debug("Request to update HandCraftVi : {}", handCraftVi);
        return handCraftViRepository.save(handCraftVi);
    }

    /**
     * Partially update a handCraftVi.
     *
     * @param handCraftVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HandCraftVi> partialUpdate(HandCraftVi handCraftVi) {
        LOG.debug("Request to partially update HandCraftVi : {}", handCraftVi);

        return handCraftViRepository.findById(handCraftVi.getId()).map(handCraftViRepository::save);
    }

    /**
     * Get one handCraftVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HandCraftVi> findOne(Long id) {
        LOG.debug("Request to get HandCraftVi : {}", id);
        return handCraftViRepository.findById(id);
    }

    /**
     * Delete the handCraftVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HandCraftVi : {}", id);
        handCraftViRepository.deleteById(id);
    }
}
