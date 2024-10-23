package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductMiMi;
import xyz.jhmapstruct.repository.NextProductMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductMiMi}.
 */
@Service
@Transactional
public class NextProductMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductMiMiService.class);

    private final NextProductMiMiRepository nextProductMiMiRepository;

    public NextProductMiMiService(NextProductMiMiRepository nextProductMiMiRepository) {
        this.nextProductMiMiRepository = nextProductMiMiRepository;
    }

    /**
     * Save a nextProductMiMi.
     *
     * @param nextProductMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextProductMiMi save(NextProductMiMi nextProductMiMi) {
        LOG.debug("Request to save NextProductMiMi : {}", nextProductMiMi);
        return nextProductMiMiRepository.save(nextProductMiMi);
    }

    /**
     * Update a nextProductMiMi.
     *
     * @param nextProductMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextProductMiMi update(NextProductMiMi nextProductMiMi) {
        LOG.debug("Request to update NextProductMiMi : {}", nextProductMiMi);
        return nextProductMiMiRepository.save(nextProductMiMi);
    }

    /**
     * Partially update a nextProductMiMi.
     *
     * @param nextProductMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductMiMi> partialUpdate(NextProductMiMi nextProductMiMi) {
        LOG.debug("Request to partially update NextProductMiMi : {}", nextProductMiMi);

        return nextProductMiMiRepository
            .findById(nextProductMiMi.getId())
            .map(existingNextProductMiMi -> {
                if (nextProductMiMi.getName() != null) {
                    existingNextProductMiMi.setName(nextProductMiMi.getName());
                }
                if (nextProductMiMi.getPrice() != null) {
                    existingNextProductMiMi.setPrice(nextProductMiMi.getPrice());
                }
                if (nextProductMiMi.getStock() != null) {
                    existingNextProductMiMi.setStock(nextProductMiMi.getStock());
                }
                if (nextProductMiMi.getDescription() != null) {
                    existingNextProductMiMi.setDescription(nextProductMiMi.getDescription());
                }

                return existingNextProductMiMi;
            })
            .map(nextProductMiMiRepository::save);
    }

    /**
     * Get all the nextProductMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductMiMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextProductMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductMiMi> findOne(Long id) {
        LOG.debug("Request to get NextProductMiMi : {}", id);
        return nextProductMiMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextProductMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductMiMi : {}", id);
        nextProductMiMiRepository.deleteById(id);
    }
}
