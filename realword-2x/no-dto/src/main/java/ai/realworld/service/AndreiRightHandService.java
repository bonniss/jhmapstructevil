package ai.realworld.service;

import ai.realworld.domain.AndreiRightHand;
import ai.realworld.repository.AndreiRightHandRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AndreiRightHand}.
 */
@Service
@Transactional
public class AndreiRightHandService {

    private static final Logger LOG = LoggerFactory.getLogger(AndreiRightHandService.class);

    private final AndreiRightHandRepository andreiRightHandRepository;

    public AndreiRightHandService(AndreiRightHandRepository andreiRightHandRepository) {
        this.andreiRightHandRepository = andreiRightHandRepository;
    }

    /**
     * Save a andreiRightHand.
     *
     * @param andreiRightHand the entity to save.
     * @return the persisted entity.
     */
    public AndreiRightHand save(AndreiRightHand andreiRightHand) {
        LOG.debug("Request to save AndreiRightHand : {}", andreiRightHand);
        return andreiRightHandRepository.save(andreiRightHand);
    }

    /**
     * Update a andreiRightHand.
     *
     * @param andreiRightHand the entity to save.
     * @return the persisted entity.
     */
    public AndreiRightHand update(AndreiRightHand andreiRightHand) {
        LOG.debug("Request to update AndreiRightHand : {}", andreiRightHand);
        return andreiRightHandRepository.save(andreiRightHand);
    }

    /**
     * Partially update a andreiRightHand.
     *
     * @param andreiRightHand the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AndreiRightHand> partialUpdate(AndreiRightHand andreiRightHand) {
        LOG.debug("Request to partially update AndreiRightHand : {}", andreiRightHand);

        return andreiRightHandRepository
            .findById(andreiRightHand.getId())
            .map(existingAndreiRightHand -> {
                if (andreiRightHand.getDetails() != null) {
                    existingAndreiRightHand.setDetails(andreiRightHand.getDetails());
                }
                if (andreiRightHand.getLat() != null) {
                    existingAndreiRightHand.setLat(andreiRightHand.getLat());
                }
                if (andreiRightHand.getLon() != null) {
                    existingAndreiRightHand.setLon(andreiRightHand.getLon());
                }

                return existingAndreiRightHand;
            })
            .map(andreiRightHandRepository::save);
    }

    /**
     * Get one andreiRightHand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AndreiRightHand> findOne(Long id) {
        LOG.debug("Request to get AndreiRightHand : {}", id);
        return andreiRightHandRepository.findById(id);
    }

    /**
     * Delete the andreiRightHand by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AndreiRightHand : {}", id);
        andreiRightHandRepository.deleteById(id);
    }
}
