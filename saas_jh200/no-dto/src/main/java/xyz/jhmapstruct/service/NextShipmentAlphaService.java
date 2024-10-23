package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentAlpha;
import xyz.jhmapstruct.repository.NextShipmentAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentAlpha}.
 */
@Service
@Transactional
public class NextShipmentAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentAlphaService.class);

    private final NextShipmentAlphaRepository nextShipmentAlphaRepository;

    public NextShipmentAlphaService(NextShipmentAlphaRepository nextShipmentAlphaRepository) {
        this.nextShipmentAlphaRepository = nextShipmentAlphaRepository;
    }

    /**
     * Save a nextShipmentAlpha.
     *
     * @param nextShipmentAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentAlpha save(NextShipmentAlpha nextShipmentAlpha) {
        LOG.debug("Request to save NextShipmentAlpha : {}", nextShipmentAlpha);
        return nextShipmentAlphaRepository.save(nextShipmentAlpha);
    }

    /**
     * Update a nextShipmentAlpha.
     *
     * @param nextShipmentAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentAlpha update(NextShipmentAlpha nextShipmentAlpha) {
        LOG.debug("Request to update NextShipmentAlpha : {}", nextShipmentAlpha);
        return nextShipmentAlphaRepository.save(nextShipmentAlpha);
    }

    /**
     * Partially update a nextShipmentAlpha.
     *
     * @param nextShipmentAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentAlpha> partialUpdate(NextShipmentAlpha nextShipmentAlpha) {
        LOG.debug("Request to partially update NextShipmentAlpha : {}", nextShipmentAlpha);

        return nextShipmentAlphaRepository
            .findById(nextShipmentAlpha.getId())
            .map(existingNextShipmentAlpha -> {
                if (nextShipmentAlpha.getTrackingNumber() != null) {
                    existingNextShipmentAlpha.setTrackingNumber(nextShipmentAlpha.getTrackingNumber());
                }
                if (nextShipmentAlpha.getShippedDate() != null) {
                    existingNextShipmentAlpha.setShippedDate(nextShipmentAlpha.getShippedDate());
                }
                if (nextShipmentAlpha.getDeliveryDate() != null) {
                    existingNextShipmentAlpha.setDeliveryDate(nextShipmentAlpha.getDeliveryDate());
                }

                return existingNextShipmentAlpha;
            })
            .map(nextShipmentAlphaRepository::save);
    }

    /**
     * Get one nextShipmentAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentAlpha> findOne(Long id) {
        LOG.debug("Request to get NextShipmentAlpha : {}", id);
        return nextShipmentAlphaRepository.findById(id);
    }

    /**
     * Delete the nextShipmentAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentAlpha : {}", id);
        nextShipmentAlphaRepository.deleteById(id);
    }
}
