package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductViVi;
import xyz.jhmapstruct.repository.NextProductViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductViVi}.
 */
@Service
@Transactional
public class NextProductViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductViViService.class);

    private final NextProductViViRepository nextProductViViRepository;

    public NextProductViViService(NextProductViViRepository nextProductViViRepository) {
        this.nextProductViViRepository = nextProductViViRepository;
    }

    /**
     * Save a nextProductViVi.
     *
     * @param nextProductViVi the entity to save.
     * @return the persisted entity.
     */
    public NextProductViVi save(NextProductViVi nextProductViVi) {
        LOG.debug("Request to save NextProductViVi : {}", nextProductViVi);
        return nextProductViViRepository.save(nextProductViVi);
    }

    /**
     * Update a nextProductViVi.
     *
     * @param nextProductViVi the entity to save.
     * @return the persisted entity.
     */
    public NextProductViVi update(NextProductViVi nextProductViVi) {
        LOG.debug("Request to update NextProductViVi : {}", nextProductViVi);
        return nextProductViViRepository.save(nextProductViVi);
    }

    /**
     * Partially update a nextProductViVi.
     *
     * @param nextProductViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductViVi> partialUpdate(NextProductViVi nextProductViVi) {
        LOG.debug("Request to partially update NextProductViVi : {}", nextProductViVi);

        return nextProductViViRepository
            .findById(nextProductViVi.getId())
            .map(existingNextProductViVi -> {
                if (nextProductViVi.getName() != null) {
                    existingNextProductViVi.setName(nextProductViVi.getName());
                }
                if (nextProductViVi.getPrice() != null) {
                    existingNextProductViVi.setPrice(nextProductViVi.getPrice());
                }
                if (nextProductViVi.getStock() != null) {
                    existingNextProductViVi.setStock(nextProductViVi.getStock());
                }
                if (nextProductViVi.getDescription() != null) {
                    existingNextProductViVi.setDescription(nextProductViVi.getDescription());
                }

                return existingNextProductViVi;
            })
            .map(nextProductViViRepository::save);
    }

    /**
     * Get all the nextProductViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductViVi> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductViViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextProductViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductViVi> findOne(Long id) {
        LOG.debug("Request to get NextProductViVi : {}", id);
        return nextProductViViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextProductViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductViVi : {}", id);
        nextProductViViRepository.deleteById(id);
    }
}
