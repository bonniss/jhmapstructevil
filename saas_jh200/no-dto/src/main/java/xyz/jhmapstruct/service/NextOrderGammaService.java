package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderGamma;
import xyz.jhmapstruct.repository.NextOrderGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderGamma}.
 */
@Service
@Transactional
public class NextOrderGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderGammaService.class);

    private final NextOrderGammaRepository nextOrderGammaRepository;

    public NextOrderGammaService(NextOrderGammaRepository nextOrderGammaRepository) {
        this.nextOrderGammaRepository = nextOrderGammaRepository;
    }

    /**
     * Save a nextOrderGamma.
     *
     * @param nextOrderGamma the entity to save.
     * @return the persisted entity.
     */
    public NextOrderGamma save(NextOrderGamma nextOrderGamma) {
        LOG.debug("Request to save NextOrderGamma : {}", nextOrderGamma);
        return nextOrderGammaRepository.save(nextOrderGamma);
    }

    /**
     * Update a nextOrderGamma.
     *
     * @param nextOrderGamma the entity to save.
     * @return the persisted entity.
     */
    public NextOrderGamma update(NextOrderGamma nextOrderGamma) {
        LOG.debug("Request to update NextOrderGamma : {}", nextOrderGamma);
        return nextOrderGammaRepository.save(nextOrderGamma);
    }

    /**
     * Partially update a nextOrderGamma.
     *
     * @param nextOrderGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderGamma> partialUpdate(NextOrderGamma nextOrderGamma) {
        LOG.debug("Request to partially update NextOrderGamma : {}", nextOrderGamma);

        return nextOrderGammaRepository
            .findById(nextOrderGamma.getId())
            .map(existingNextOrderGamma -> {
                if (nextOrderGamma.getOrderDate() != null) {
                    existingNextOrderGamma.setOrderDate(nextOrderGamma.getOrderDate());
                }
                if (nextOrderGamma.getTotalPrice() != null) {
                    existingNextOrderGamma.setTotalPrice(nextOrderGamma.getTotalPrice());
                }
                if (nextOrderGamma.getStatus() != null) {
                    existingNextOrderGamma.setStatus(nextOrderGamma.getStatus());
                }

                return existingNextOrderGamma;
            })
            .map(nextOrderGammaRepository::save);
    }

    /**
     * Get all the nextOrderGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderGamma> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderGammaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextOrderGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderGamma> findOne(Long id) {
        LOG.debug("Request to get NextOrderGamma : {}", id);
        return nextOrderGammaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextOrderGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderGamma : {}", id);
        nextOrderGammaRepository.deleteById(id);
    }
}
