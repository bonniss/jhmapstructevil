package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentGamma;
import xyz.jhmapstruct.repository.NextShipmentGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentGamma}.
 */
@Service
@Transactional
public class NextShipmentGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentGammaService.class);

    private final NextShipmentGammaRepository nextShipmentGammaRepository;

    public NextShipmentGammaService(NextShipmentGammaRepository nextShipmentGammaRepository) {
        this.nextShipmentGammaRepository = nextShipmentGammaRepository;
    }

    /**
     * Save a nextShipmentGamma.
     *
     * @param nextShipmentGamma the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentGamma save(NextShipmentGamma nextShipmentGamma) {
        LOG.debug("Request to save NextShipmentGamma : {}", nextShipmentGamma);
        return nextShipmentGammaRepository.save(nextShipmentGamma);
    }

    /**
     * Update a nextShipmentGamma.
     *
     * @param nextShipmentGamma the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentGamma update(NextShipmentGamma nextShipmentGamma) {
        LOG.debug("Request to update NextShipmentGamma : {}", nextShipmentGamma);
        return nextShipmentGammaRepository.save(nextShipmentGamma);
    }

    /**
     * Partially update a nextShipmentGamma.
     *
     * @param nextShipmentGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentGamma> partialUpdate(NextShipmentGamma nextShipmentGamma) {
        LOG.debug("Request to partially update NextShipmentGamma : {}", nextShipmentGamma);

        return nextShipmentGammaRepository
            .findById(nextShipmentGamma.getId())
            .map(existingNextShipmentGamma -> {
                if (nextShipmentGamma.getTrackingNumber() != null) {
                    existingNextShipmentGamma.setTrackingNumber(nextShipmentGamma.getTrackingNumber());
                }
                if (nextShipmentGamma.getShippedDate() != null) {
                    existingNextShipmentGamma.setShippedDate(nextShipmentGamma.getShippedDate());
                }
                if (nextShipmentGamma.getDeliveryDate() != null) {
                    existingNextShipmentGamma.setDeliveryDate(nextShipmentGamma.getDeliveryDate());
                }

                return existingNextShipmentGamma;
            })
            .map(nextShipmentGammaRepository::save);
    }

    /**
     * Get one nextShipmentGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentGamma> findOne(Long id) {
        LOG.debug("Request to get NextShipmentGamma : {}", id);
        return nextShipmentGammaRepository.findById(id);
    }

    /**
     * Delete the nextShipmentGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentGamma : {}", id);
        nextShipmentGammaRepository.deleteById(id);
    }
}
