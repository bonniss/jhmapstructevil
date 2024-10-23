package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductVi;
import xyz.jhmapstruct.repository.NextProductViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductVi}.
 */
@Service
@Transactional
public class NextProductViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductViService.class);

    private final NextProductViRepository nextProductViRepository;

    public NextProductViService(NextProductViRepository nextProductViRepository) {
        this.nextProductViRepository = nextProductViRepository;
    }

    /**
     * Save a nextProductVi.
     *
     * @param nextProductVi the entity to save.
     * @return the persisted entity.
     */
    public NextProductVi save(NextProductVi nextProductVi) {
        LOG.debug("Request to save NextProductVi : {}", nextProductVi);
        return nextProductViRepository.save(nextProductVi);
    }

    /**
     * Update a nextProductVi.
     *
     * @param nextProductVi the entity to save.
     * @return the persisted entity.
     */
    public NextProductVi update(NextProductVi nextProductVi) {
        LOG.debug("Request to update NextProductVi : {}", nextProductVi);
        return nextProductViRepository.save(nextProductVi);
    }

    /**
     * Partially update a nextProductVi.
     *
     * @param nextProductVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductVi> partialUpdate(NextProductVi nextProductVi) {
        LOG.debug("Request to partially update NextProductVi : {}", nextProductVi);

        return nextProductViRepository
            .findById(nextProductVi.getId())
            .map(existingNextProductVi -> {
                if (nextProductVi.getName() != null) {
                    existingNextProductVi.setName(nextProductVi.getName());
                }
                if (nextProductVi.getPrice() != null) {
                    existingNextProductVi.setPrice(nextProductVi.getPrice());
                }
                if (nextProductVi.getStock() != null) {
                    existingNextProductVi.setStock(nextProductVi.getStock());
                }
                if (nextProductVi.getDescription() != null) {
                    existingNextProductVi.setDescription(nextProductVi.getDescription());
                }

                return existingNextProductVi;
            })
            .map(nextProductViRepository::save);
    }

    /**
     * Get all the nextProductVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductVi> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextProductVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductVi> findOne(Long id) {
        LOG.debug("Request to get NextProductVi : {}", id);
        return nextProductViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextProductVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductVi : {}", id);
        nextProductViRepository.deleteById(id);
    }
}
