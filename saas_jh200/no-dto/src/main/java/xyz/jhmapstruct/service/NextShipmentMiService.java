package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentMi;
import xyz.jhmapstruct.repository.NextShipmentMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentMi}.
 */
@Service
@Transactional
public class NextShipmentMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentMiService.class);

    private final NextShipmentMiRepository nextShipmentMiRepository;

    public NextShipmentMiService(NextShipmentMiRepository nextShipmentMiRepository) {
        this.nextShipmentMiRepository = nextShipmentMiRepository;
    }

    /**
     * Save a nextShipmentMi.
     *
     * @param nextShipmentMi the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentMi save(NextShipmentMi nextShipmentMi) {
        LOG.debug("Request to save NextShipmentMi : {}", nextShipmentMi);
        return nextShipmentMiRepository.save(nextShipmentMi);
    }

    /**
     * Update a nextShipmentMi.
     *
     * @param nextShipmentMi the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentMi update(NextShipmentMi nextShipmentMi) {
        LOG.debug("Request to update NextShipmentMi : {}", nextShipmentMi);
        return nextShipmentMiRepository.save(nextShipmentMi);
    }

    /**
     * Partially update a nextShipmentMi.
     *
     * @param nextShipmentMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentMi> partialUpdate(NextShipmentMi nextShipmentMi) {
        LOG.debug("Request to partially update NextShipmentMi : {}", nextShipmentMi);

        return nextShipmentMiRepository
            .findById(nextShipmentMi.getId())
            .map(existingNextShipmentMi -> {
                if (nextShipmentMi.getTrackingNumber() != null) {
                    existingNextShipmentMi.setTrackingNumber(nextShipmentMi.getTrackingNumber());
                }
                if (nextShipmentMi.getShippedDate() != null) {
                    existingNextShipmentMi.setShippedDate(nextShipmentMi.getShippedDate());
                }
                if (nextShipmentMi.getDeliveryDate() != null) {
                    existingNextShipmentMi.setDeliveryDate(nextShipmentMi.getDeliveryDate());
                }

                return existingNextShipmentMi;
            })
            .map(nextShipmentMiRepository::save);
    }

    /**
     * Get one nextShipmentMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentMi> findOne(Long id) {
        LOG.debug("Request to get NextShipmentMi : {}", id);
        return nextShipmentMiRepository.findById(id);
    }

    /**
     * Delete the nextShipmentMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentMi : {}", id);
        nextShipmentMiRepository.deleteById(id);
    }
}
