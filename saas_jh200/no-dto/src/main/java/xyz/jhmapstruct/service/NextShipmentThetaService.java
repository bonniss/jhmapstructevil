package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentTheta;
import xyz.jhmapstruct.repository.NextShipmentThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentTheta}.
 */
@Service
@Transactional
public class NextShipmentThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentThetaService.class);

    private final NextShipmentThetaRepository nextShipmentThetaRepository;

    public NextShipmentThetaService(NextShipmentThetaRepository nextShipmentThetaRepository) {
        this.nextShipmentThetaRepository = nextShipmentThetaRepository;
    }

    /**
     * Save a nextShipmentTheta.
     *
     * @param nextShipmentTheta the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentTheta save(NextShipmentTheta nextShipmentTheta) {
        LOG.debug("Request to save NextShipmentTheta : {}", nextShipmentTheta);
        return nextShipmentThetaRepository.save(nextShipmentTheta);
    }

    /**
     * Update a nextShipmentTheta.
     *
     * @param nextShipmentTheta the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentTheta update(NextShipmentTheta nextShipmentTheta) {
        LOG.debug("Request to update NextShipmentTheta : {}", nextShipmentTheta);
        return nextShipmentThetaRepository.save(nextShipmentTheta);
    }

    /**
     * Partially update a nextShipmentTheta.
     *
     * @param nextShipmentTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentTheta> partialUpdate(NextShipmentTheta nextShipmentTheta) {
        LOG.debug("Request to partially update NextShipmentTheta : {}", nextShipmentTheta);

        return nextShipmentThetaRepository
            .findById(nextShipmentTheta.getId())
            .map(existingNextShipmentTheta -> {
                if (nextShipmentTheta.getTrackingNumber() != null) {
                    existingNextShipmentTheta.setTrackingNumber(nextShipmentTheta.getTrackingNumber());
                }
                if (nextShipmentTheta.getShippedDate() != null) {
                    existingNextShipmentTheta.setShippedDate(nextShipmentTheta.getShippedDate());
                }
                if (nextShipmentTheta.getDeliveryDate() != null) {
                    existingNextShipmentTheta.setDeliveryDate(nextShipmentTheta.getDeliveryDate());
                }

                return existingNextShipmentTheta;
            })
            .map(nextShipmentThetaRepository::save);
    }

    /**
     * Get one nextShipmentTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentTheta> findOne(Long id) {
        LOG.debug("Request to get NextShipmentTheta : {}", id);
        return nextShipmentThetaRepository.findById(id);
    }

    /**
     * Delete the nextShipmentTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentTheta : {}", id);
        nextShipmentThetaRepository.deleteById(id);
    }
}
