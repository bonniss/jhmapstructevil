package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentMiMi;
import xyz.jhmapstruct.repository.NextShipmentMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentMiMi}.
 */
@Service
@Transactional
public class NextShipmentMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentMiMiService.class);

    private final NextShipmentMiMiRepository nextShipmentMiMiRepository;

    public NextShipmentMiMiService(NextShipmentMiMiRepository nextShipmentMiMiRepository) {
        this.nextShipmentMiMiRepository = nextShipmentMiMiRepository;
    }

    /**
     * Save a nextShipmentMiMi.
     *
     * @param nextShipmentMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentMiMi save(NextShipmentMiMi nextShipmentMiMi) {
        LOG.debug("Request to save NextShipmentMiMi : {}", nextShipmentMiMi);
        return nextShipmentMiMiRepository.save(nextShipmentMiMi);
    }

    /**
     * Update a nextShipmentMiMi.
     *
     * @param nextShipmentMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentMiMi update(NextShipmentMiMi nextShipmentMiMi) {
        LOG.debug("Request to update NextShipmentMiMi : {}", nextShipmentMiMi);
        return nextShipmentMiMiRepository.save(nextShipmentMiMi);
    }

    /**
     * Partially update a nextShipmentMiMi.
     *
     * @param nextShipmentMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentMiMi> partialUpdate(NextShipmentMiMi nextShipmentMiMi) {
        LOG.debug("Request to partially update NextShipmentMiMi : {}", nextShipmentMiMi);

        return nextShipmentMiMiRepository
            .findById(nextShipmentMiMi.getId())
            .map(existingNextShipmentMiMi -> {
                if (nextShipmentMiMi.getTrackingNumber() != null) {
                    existingNextShipmentMiMi.setTrackingNumber(nextShipmentMiMi.getTrackingNumber());
                }
                if (nextShipmentMiMi.getShippedDate() != null) {
                    existingNextShipmentMiMi.setShippedDate(nextShipmentMiMi.getShippedDate());
                }
                if (nextShipmentMiMi.getDeliveryDate() != null) {
                    existingNextShipmentMiMi.setDeliveryDate(nextShipmentMiMi.getDeliveryDate());
                }

                return existingNextShipmentMiMi;
            })
            .map(nextShipmentMiMiRepository::save);
    }

    /**
     * Get one nextShipmentMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentMiMi> findOne(Long id) {
        LOG.debug("Request to get NextShipmentMiMi : {}", id);
        return nextShipmentMiMiRepository.findById(id);
    }

    /**
     * Delete the nextShipmentMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentMiMi : {}", id);
        nextShipmentMiMiRepository.deleteById(id);
    }
}
