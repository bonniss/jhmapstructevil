package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentBeta;
import xyz.jhmapstruct.repository.NextShipmentBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentBeta}.
 */
@Service
@Transactional
public class NextShipmentBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentBetaService.class);

    private final NextShipmentBetaRepository nextShipmentBetaRepository;

    public NextShipmentBetaService(NextShipmentBetaRepository nextShipmentBetaRepository) {
        this.nextShipmentBetaRepository = nextShipmentBetaRepository;
    }

    /**
     * Save a nextShipmentBeta.
     *
     * @param nextShipmentBeta the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentBeta save(NextShipmentBeta nextShipmentBeta) {
        LOG.debug("Request to save NextShipmentBeta : {}", nextShipmentBeta);
        return nextShipmentBetaRepository.save(nextShipmentBeta);
    }

    /**
     * Update a nextShipmentBeta.
     *
     * @param nextShipmentBeta the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentBeta update(NextShipmentBeta nextShipmentBeta) {
        LOG.debug("Request to update NextShipmentBeta : {}", nextShipmentBeta);
        return nextShipmentBetaRepository.save(nextShipmentBeta);
    }

    /**
     * Partially update a nextShipmentBeta.
     *
     * @param nextShipmentBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentBeta> partialUpdate(NextShipmentBeta nextShipmentBeta) {
        LOG.debug("Request to partially update NextShipmentBeta : {}", nextShipmentBeta);

        return nextShipmentBetaRepository
            .findById(nextShipmentBeta.getId())
            .map(existingNextShipmentBeta -> {
                if (nextShipmentBeta.getTrackingNumber() != null) {
                    existingNextShipmentBeta.setTrackingNumber(nextShipmentBeta.getTrackingNumber());
                }
                if (nextShipmentBeta.getShippedDate() != null) {
                    existingNextShipmentBeta.setShippedDate(nextShipmentBeta.getShippedDate());
                }
                if (nextShipmentBeta.getDeliveryDate() != null) {
                    existingNextShipmentBeta.setDeliveryDate(nextShipmentBeta.getDeliveryDate());
                }

                return existingNextShipmentBeta;
            })
            .map(nextShipmentBetaRepository::save);
    }

    /**
     * Get one nextShipmentBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentBeta> findOne(Long id) {
        LOG.debug("Request to get NextShipmentBeta : {}", id);
        return nextShipmentBetaRepository.findById(id);
    }

    /**
     * Delete the nextShipmentBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentBeta : {}", id);
        nextShipmentBetaRepository.deleteById(id);
    }
}
