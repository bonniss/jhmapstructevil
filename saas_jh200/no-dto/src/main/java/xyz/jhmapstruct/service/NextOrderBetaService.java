package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderBeta;
import xyz.jhmapstruct.repository.NextOrderBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderBeta}.
 */
@Service
@Transactional
public class NextOrderBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderBetaService.class);

    private final NextOrderBetaRepository nextOrderBetaRepository;

    public NextOrderBetaService(NextOrderBetaRepository nextOrderBetaRepository) {
        this.nextOrderBetaRepository = nextOrderBetaRepository;
    }

    /**
     * Save a nextOrderBeta.
     *
     * @param nextOrderBeta the entity to save.
     * @return the persisted entity.
     */
    public NextOrderBeta save(NextOrderBeta nextOrderBeta) {
        LOG.debug("Request to save NextOrderBeta : {}", nextOrderBeta);
        return nextOrderBetaRepository.save(nextOrderBeta);
    }

    /**
     * Update a nextOrderBeta.
     *
     * @param nextOrderBeta the entity to save.
     * @return the persisted entity.
     */
    public NextOrderBeta update(NextOrderBeta nextOrderBeta) {
        LOG.debug("Request to update NextOrderBeta : {}", nextOrderBeta);
        return nextOrderBetaRepository.save(nextOrderBeta);
    }

    /**
     * Partially update a nextOrderBeta.
     *
     * @param nextOrderBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderBeta> partialUpdate(NextOrderBeta nextOrderBeta) {
        LOG.debug("Request to partially update NextOrderBeta : {}", nextOrderBeta);

        return nextOrderBetaRepository
            .findById(nextOrderBeta.getId())
            .map(existingNextOrderBeta -> {
                if (nextOrderBeta.getOrderDate() != null) {
                    existingNextOrderBeta.setOrderDate(nextOrderBeta.getOrderDate());
                }
                if (nextOrderBeta.getTotalPrice() != null) {
                    existingNextOrderBeta.setTotalPrice(nextOrderBeta.getTotalPrice());
                }
                if (nextOrderBeta.getStatus() != null) {
                    existingNextOrderBeta.setStatus(nextOrderBeta.getStatus());
                }

                return existingNextOrderBeta;
            })
            .map(nextOrderBetaRepository::save);
    }

    /**
     * Get all the nextOrderBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderBeta> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderBetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextOrderBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderBeta> findOne(Long id) {
        LOG.debug("Request to get NextOrderBeta : {}", id);
        return nextOrderBetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextOrderBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderBeta : {}", id);
        nextOrderBetaRepository.deleteById(id);
    }
}
