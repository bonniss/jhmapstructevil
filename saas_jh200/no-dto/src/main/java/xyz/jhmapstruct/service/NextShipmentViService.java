package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentVi;
import xyz.jhmapstruct.repository.NextShipmentViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentVi}.
 */
@Service
@Transactional
public class NextShipmentViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentViService.class);

    private final NextShipmentViRepository nextShipmentViRepository;

    public NextShipmentViService(NextShipmentViRepository nextShipmentViRepository) {
        this.nextShipmentViRepository = nextShipmentViRepository;
    }

    /**
     * Save a nextShipmentVi.
     *
     * @param nextShipmentVi the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentVi save(NextShipmentVi nextShipmentVi) {
        LOG.debug("Request to save NextShipmentVi : {}", nextShipmentVi);
        return nextShipmentViRepository.save(nextShipmentVi);
    }

    /**
     * Update a nextShipmentVi.
     *
     * @param nextShipmentVi the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentVi update(NextShipmentVi nextShipmentVi) {
        LOG.debug("Request to update NextShipmentVi : {}", nextShipmentVi);
        return nextShipmentViRepository.save(nextShipmentVi);
    }

    /**
     * Partially update a nextShipmentVi.
     *
     * @param nextShipmentVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentVi> partialUpdate(NextShipmentVi nextShipmentVi) {
        LOG.debug("Request to partially update NextShipmentVi : {}", nextShipmentVi);

        return nextShipmentViRepository
            .findById(nextShipmentVi.getId())
            .map(existingNextShipmentVi -> {
                if (nextShipmentVi.getTrackingNumber() != null) {
                    existingNextShipmentVi.setTrackingNumber(nextShipmentVi.getTrackingNumber());
                }
                if (nextShipmentVi.getShippedDate() != null) {
                    existingNextShipmentVi.setShippedDate(nextShipmentVi.getShippedDate());
                }
                if (nextShipmentVi.getDeliveryDate() != null) {
                    existingNextShipmentVi.setDeliveryDate(nextShipmentVi.getDeliveryDate());
                }

                return existingNextShipmentVi;
            })
            .map(nextShipmentViRepository::save);
    }

    /**
     * Get one nextShipmentVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentVi> findOne(Long id) {
        LOG.debug("Request to get NextShipmentVi : {}", id);
        return nextShipmentViRepository.findById(id);
    }

    /**
     * Delete the nextShipmentVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentVi : {}", id);
        nextShipmentViRepository.deleteById(id);
    }
}
