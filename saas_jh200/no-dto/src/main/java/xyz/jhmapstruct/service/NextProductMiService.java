package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductMi;
import xyz.jhmapstruct.repository.NextProductMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductMi}.
 */
@Service
@Transactional
public class NextProductMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductMiService.class);

    private final NextProductMiRepository nextProductMiRepository;

    public NextProductMiService(NextProductMiRepository nextProductMiRepository) {
        this.nextProductMiRepository = nextProductMiRepository;
    }

    /**
     * Save a nextProductMi.
     *
     * @param nextProductMi the entity to save.
     * @return the persisted entity.
     */
    public NextProductMi save(NextProductMi nextProductMi) {
        LOG.debug("Request to save NextProductMi : {}", nextProductMi);
        return nextProductMiRepository.save(nextProductMi);
    }

    /**
     * Update a nextProductMi.
     *
     * @param nextProductMi the entity to save.
     * @return the persisted entity.
     */
    public NextProductMi update(NextProductMi nextProductMi) {
        LOG.debug("Request to update NextProductMi : {}", nextProductMi);
        return nextProductMiRepository.save(nextProductMi);
    }

    /**
     * Partially update a nextProductMi.
     *
     * @param nextProductMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductMi> partialUpdate(NextProductMi nextProductMi) {
        LOG.debug("Request to partially update NextProductMi : {}", nextProductMi);

        return nextProductMiRepository
            .findById(nextProductMi.getId())
            .map(existingNextProductMi -> {
                if (nextProductMi.getName() != null) {
                    existingNextProductMi.setName(nextProductMi.getName());
                }
                if (nextProductMi.getPrice() != null) {
                    existingNextProductMi.setPrice(nextProductMi.getPrice());
                }
                if (nextProductMi.getStock() != null) {
                    existingNextProductMi.setStock(nextProductMi.getStock());
                }
                if (nextProductMi.getDescription() != null) {
                    existingNextProductMi.setDescription(nextProductMi.getDescription());
                }

                return existingNextProductMi;
            })
            .map(nextProductMiRepository::save);
    }

    /**
     * Get all the nextProductMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductMi> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextProductMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductMi> findOne(Long id) {
        LOG.debug("Request to get NextProductMi : {}", id);
        return nextProductMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextProductMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductMi : {}", id);
        nextProductMiRepository.deleteById(id);
    }
}
