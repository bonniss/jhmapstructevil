package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipment;
import xyz.jhmapstruct.repository.NextShipmentRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipment}.
 */
@Service
@Transactional
public class NextShipmentService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentService.class);

    private final NextShipmentRepository nextShipmentRepository;

    public NextShipmentService(NextShipmentRepository nextShipmentRepository) {
        this.nextShipmentRepository = nextShipmentRepository;
    }

    /**
     * Save a nextShipment.
     *
     * @param nextShipment the entity to save.
     * @return the persisted entity.
     */
    public NextShipment save(NextShipment nextShipment) {
        LOG.debug("Request to save NextShipment : {}", nextShipment);
        return nextShipmentRepository.save(nextShipment);
    }

    /**
     * Update a nextShipment.
     *
     * @param nextShipment the entity to save.
     * @return the persisted entity.
     */
    public NextShipment update(NextShipment nextShipment) {
        LOG.debug("Request to update NextShipment : {}", nextShipment);
        return nextShipmentRepository.save(nextShipment);
    }

    /**
     * Partially update a nextShipment.
     *
     * @param nextShipment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipment> partialUpdate(NextShipment nextShipment) {
        LOG.debug("Request to partially update NextShipment : {}", nextShipment);

        return nextShipmentRepository
            .findById(nextShipment.getId())
            .map(existingNextShipment -> {
                if (nextShipment.getTrackingNumber() != null) {
                    existingNextShipment.setTrackingNumber(nextShipment.getTrackingNumber());
                }
                if (nextShipment.getShippedDate() != null) {
                    existingNextShipment.setShippedDate(nextShipment.getShippedDate());
                }
                if (nextShipment.getDeliveryDate() != null) {
                    existingNextShipment.setDeliveryDate(nextShipment.getDeliveryDate());
                }

                return existingNextShipment;
            })
            .map(nextShipmentRepository::save);
    }

    /**
     * Get one nextShipment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipment> findOne(Long id) {
        LOG.debug("Request to get NextShipment : {}", id);
        return nextShipmentRepository.findById(id);
    }

    /**
     * Delete the nextShipment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipment : {}", id);
        nextShipmentRepository.deleteById(id);
    }
}
