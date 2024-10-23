package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentViVi;
import xyz.jhmapstruct.repository.NextShipmentViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentViVi}.
 */
@Service
@Transactional
public class NextShipmentViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentViViService.class);

    private final NextShipmentViViRepository nextShipmentViViRepository;

    public NextShipmentViViService(NextShipmentViViRepository nextShipmentViViRepository) {
        this.nextShipmentViViRepository = nextShipmentViViRepository;
    }

    /**
     * Save a nextShipmentViVi.
     *
     * @param nextShipmentViVi the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentViVi save(NextShipmentViVi nextShipmentViVi) {
        LOG.debug("Request to save NextShipmentViVi : {}", nextShipmentViVi);
        return nextShipmentViViRepository.save(nextShipmentViVi);
    }

    /**
     * Update a nextShipmentViVi.
     *
     * @param nextShipmentViVi the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentViVi update(NextShipmentViVi nextShipmentViVi) {
        LOG.debug("Request to update NextShipmentViVi : {}", nextShipmentViVi);
        return nextShipmentViViRepository.save(nextShipmentViVi);
    }

    /**
     * Partially update a nextShipmentViVi.
     *
     * @param nextShipmentViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentViVi> partialUpdate(NextShipmentViVi nextShipmentViVi) {
        LOG.debug("Request to partially update NextShipmentViVi : {}", nextShipmentViVi);

        return nextShipmentViViRepository
            .findById(nextShipmentViVi.getId())
            .map(existingNextShipmentViVi -> {
                if (nextShipmentViVi.getTrackingNumber() != null) {
                    existingNextShipmentViVi.setTrackingNumber(nextShipmentViVi.getTrackingNumber());
                }
                if (nextShipmentViVi.getShippedDate() != null) {
                    existingNextShipmentViVi.setShippedDate(nextShipmentViVi.getShippedDate());
                }
                if (nextShipmentViVi.getDeliveryDate() != null) {
                    existingNextShipmentViVi.setDeliveryDate(nextShipmentViVi.getDeliveryDate());
                }

                return existingNextShipmentViVi;
            })
            .map(nextShipmentViViRepository::save);
    }

    /**
     * Get one nextShipmentViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentViVi> findOne(Long id) {
        LOG.debug("Request to get NextShipmentViVi : {}", id);
        return nextShipmentViViRepository.findById(id);
    }

    /**
     * Delete the nextShipmentViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentViVi : {}", id);
        nextShipmentViViRepository.deleteById(id);
    }
}
