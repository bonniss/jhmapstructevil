package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextOrderTheta;
import xyz.jhmapstruct.repository.NextOrderThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextOrderTheta}.
 */
@Service
@Transactional
public class NextOrderThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderThetaService.class);

    private final NextOrderThetaRepository nextOrderThetaRepository;

    public NextOrderThetaService(NextOrderThetaRepository nextOrderThetaRepository) {
        this.nextOrderThetaRepository = nextOrderThetaRepository;
    }

    /**
     * Save a nextOrderTheta.
     *
     * @param nextOrderTheta the entity to save.
     * @return the persisted entity.
     */
    public NextOrderTheta save(NextOrderTheta nextOrderTheta) {
        LOG.debug("Request to save NextOrderTheta : {}", nextOrderTheta);
        return nextOrderThetaRepository.save(nextOrderTheta);
    }

    /**
     * Update a nextOrderTheta.
     *
     * @param nextOrderTheta the entity to save.
     * @return the persisted entity.
     */
    public NextOrderTheta update(NextOrderTheta nextOrderTheta) {
        LOG.debug("Request to update NextOrderTheta : {}", nextOrderTheta);
        return nextOrderThetaRepository.save(nextOrderTheta);
    }

    /**
     * Partially update a nextOrderTheta.
     *
     * @param nextOrderTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOrderTheta> partialUpdate(NextOrderTheta nextOrderTheta) {
        LOG.debug("Request to partially update NextOrderTheta : {}", nextOrderTheta);

        return nextOrderThetaRepository
            .findById(nextOrderTheta.getId())
            .map(existingNextOrderTheta -> {
                if (nextOrderTheta.getOrderDate() != null) {
                    existingNextOrderTheta.setOrderDate(nextOrderTheta.getOrderDate());
                }
                if (nextOrderTheta.getTotalPrice() != null) {
                    existingNextOrderTheta.setTotalPrice(nextOrderTheta.getTotalPrice());
                }
                if (nextOrderTheta.getStatus() != null) {
                    existingNextOrderTheta.setStatus(nextOrderTheta.getStatus());
                }

                return existingNextOrderTheta;
            })
            .map(nextOrderThetaRepository::save);
    }

    /**
     * Get all the nextOrderThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextOrderTheta> findAllWithEagerRelationships(Pageable pageable) {
        return nextOrderThetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextOrderTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOrderTheta> findOne(Long id) {
        LOG.debug("Request to get NextOrderTheta : {}", id);
        return nextOrderThetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextOrderTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextOrderTheta : {}", id);
        nextOrderThetaRepository.deleteById(id);
    }
}
