package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductAlpha;
import xyz.jhmapstruct.repository.NextProductAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductAlpha}.
 */
@Service
@Transactional
public class NextProductAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductAlphaService.class);

    private final NextProductAlphaRepository nextProductAlphaRepository;

    public NextProductAlphaService(NextProductAlphaRepository nextProductAlphaRepository) {
        this.nextProductAlphaRepository = nextProductAlphaRepository;
    }

    /**
     * Save a nextProductAlpha.
     *
     * @param nextProductAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextProductAlpha save(NextProductAlpha nextProductAlpha) {
        LOG.debug("Request to save NextProductAlpha : {}", nextProductAlpha);
        return nextProductAlphaRepository.save(nextProductAlpha);
    }

    /**
     * Update a nextProductAlpha.
     *
     * @param nextProductAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextProductAlpha update(NextProductAlpha nextProductAlpha) {
        LOG.debug("Request to update NextProductAlpha : {}", nextProductAlpha);
        return nextProductAlphaRepository.save(nextProductAlpha);
    }

    /**
     * Partially update a nextProductAlpha.
     *
     * @param nextProductAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductAlpha> partialUpdate(NextProductAlpha nextProductAlpha) {
        LOG.debug("Request to partially update NextProductAlpha : {}", nextProductAlpha);

        return nextProductAlphaRepository
            .findById(nextProductAlpha.getId())
            .map(existingNextProductAlpha -> {
                if (nextProductAlpha.getName() != null) {
                    existingNextProductAlpha.setName(nextProductAlpha.getName());
                }
                if (nextProductAlpha.getPrice() != null) {
                    existingNextProductAlpha.setPrice(nextProductAlpha.getPrice());
                }
                if (nextProductAlpha.getStock() != null) {
                    existingNextProductAlpha.setStock(nextProductAlpha.getStock());
                }
                if (nextProductAlpha.getDescription() != null) {
                    existingNextProductAlpha.setDescription(nextProductAlpha.getDescription());
                }

                return existingNextProductAlpha;
            })
            .map(nextProductAlphaRepository::save);
    }

    /**
     * Get all the nextProductAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductAlphaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextProductAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductAlpha> findOne(Long id) {
        LOG.debug("Request to get NextProductAlpha : {}", id);
        return nextProductAlphaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextProductAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductAlpha : {}", id);
        nextProductAlphaRepository.deleteById(id);
    }
}
