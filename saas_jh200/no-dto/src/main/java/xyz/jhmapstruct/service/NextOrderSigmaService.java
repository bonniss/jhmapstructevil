package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderSigma;
import xyz.jhmapstruct.repository.NextOrderSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderSigma}.
 */
@Service
@Transactional
public class NextOrderSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderSigmaService.class);

    private final NextOrderSigmaRepository nextOrderSigmaRepository;

    public NextOrderSigmaService(NextOrderSigmaRepository nextOrderSigmaRepository) {
        this.nextOrderSigmaRepository = nextOrderSigmaRepository;
    }

    /**
     * Save a nextOrderSigma.
     *
     * @param nextOrderSigma the entity to save.
     * @return the persisted entity.
     */
    public NextOrderSigma save(NextOrderSigma nextOrderSigma) {
        LOG.debug("Request to save NextOrderSigma : {}", nextOrderSigma);
        return nextOrderSigmaRepository.save(nextOrderSigma);
    }

    /**
     * Update a nextOrderSigma.
     *
     * @param nextOrderSigma the entity to save.
     * @return the persisted entity.
     */
    public NextOrderSigma update(NextOrderSigma nextOrderSigma) {
        LOG.debug("Request to update NextOrderSigma : {}", nextOrderSigma);
        return nextOrderSigmaRepository.save(nextOrderSigma);
    }

    /**
     * Partially update a nextOrderSigma.
     *
     * @param nextOrderSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderSigma> partialUpdate(NextOrderSigma nextOrderSigma) {
        LOG.debug("Request to partially update NextOrderSigma : {}", nextOrderSigma);

        return nextOrderSigmaRepository
            .findById(nextOrderSigma.getId())
            .map(existingNextOrderSigma -> {
                if (nextOrderSigma.getOrderDate() != null) {
                    existingNextOrderSigma.setOrderDate(nextOrderSigma.getOrderDate());
                }
                if (nextOrderSigma.getTotalPrice() != null) {
                    existingNextOrderSigma.setTotalPrice(nextOrderSigma.getTotalPrice());
                }
                if (nextOrderSigma.getStatus() != null) {
                    existingNextOrderSigma.setStatus(nextOrderSigma.getStatus());
                }

                return existingNextOrderSigma;
            })
            .map(nextOrderSigmaRepository::save);
    }

    /**
     * Get all the nextOrderSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderSigma> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderSigmaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextOrderSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderSigma> findOne(Long id) {
        LOG.debug("Request to get NextOrderSigma : {}", id);
        return nextOrderSigmaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextOrderSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderSigma : {}", id);
        nextOrderSigmaRepository.deleteById(id);
    }
}
