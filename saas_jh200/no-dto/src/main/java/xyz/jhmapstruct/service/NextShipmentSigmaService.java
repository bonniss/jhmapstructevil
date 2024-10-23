package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentSigma;
import xyz.jhmapstruct.repository.NextShipmentSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentSigma}.
 */
@Service
@Transactional
public class NextShipmentSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentSigmaService.class);

    private final NextShipmentSigmaRepository nextShipmentSigmaRepository;

    public NextShipmentSigmaService(NextShipmentSigmaRepository nextShipmentSigmaRepository) {
        this.nextShipmentSigmaRepository = nextShipmentSigmaRepository;
    }

    /**
     * Save a nextShipmentSigma.
     *
     * @param nextShipmentSigma the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentSigma save(NextShipmentSigma nextShipmentSigma) {
        LOG.debug("Request to save NextShipmentSigma : {}", nextShipmentSigma);
        return nextShipmentSigmaRepository.save(nextShipmentSigma);
    }

    /**
     * Update a nextShipmentSigma.
     *
     * @param nextShipmentSigma the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentSigma update(NextShipmentSigma nextShipmentSigma) {
        LOG.debug("Request to update NextShipmentSigma : {}", nextShipmentSigma);
        return nextShipmentSigmaRepository.save(nextShipmentSigma);
    }

    /**
     * Partially update a nextShipmentSigma.
     *
     * @param nextShipmentSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentSigma> partialUpdate(NextShipmentSigma nextShipmentSigma) {
        LOG.debug("Request to partially update NextShipmentSigma : {}", nextShipmentSigma);

        return nextShipmentSigmaRepository
            .findById(nextShipmentSigma.getId())
            .map(existingNextShipmentSigma -> {
                if (nextShipmentSigma.getTrackingNumber() != null) {
                    existingNextShipmentSigma.setTrackingNumber(nextShipmentSigma.getTrackingNumber());
                }
                if (nextShipmentSigma.getShippedDate() != null) {
                    existingNextShipmentSigma.setShippedDate(nextShipmentSigma.getShippedDate());
                }
                if (nextShipmentSigma.getDeliveryDate() != null) {
                    existingNextShipmentSigma.setDeliveryDate(nextShipmentSigma.getDeliveryDate());
                }

                return existingNextShipmentSigma;
            })
            .map(nextShipmentSigmaRepository::save);
    }

    /**
     * Get one nextShipmentSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentSigma> findOne(Long id) {
        LOG.debug("Request to get NextShipmentSigma : {}", id);
        return nextShipmentSigmaRepository.findById(id);
    }

    /**
     * Delete the nextShipmentSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentSigma : {}", id);
        nextShipmentSigmaRepository.deleteById(id);
    }
}
