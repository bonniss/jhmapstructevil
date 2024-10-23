package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryAlpha;
import xyz.jhmapstruct.repository.NextCategoryAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryAlpha}.
 */
@Service
@Transactional
public class NextCategoryAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryAlphaService.class);

    private final NextCategoryAlphaRepository nextCategoryAlphaRepository;

    public NextCategoryAlphaService(NextCategoryAlphaRepository nextCategoryAlphaRepository) {
        this.nextCategoryAlphaRepository = nextCategoryAlphaRepository;
    }

    /**
     * Save a nextCategoryAlpha.
     *
     * @param nextCategoryAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryAlpha save(NextCategoryAlpha nextCategoryAlpha) {
        LOG.debug("Request to save NextCategoryAlpha : {}", nextCategoryAlpha);
        return nextCategoryAlphaRepository.save(nextCategoryAlpha);
    }

    /**
     * Update a nextCategoryAlpha.
     *
     * @param nextCategoryAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryAlpha update(NextCategoryAlpha nextCategoryAlpha) {
        LOG.debug("Request to update NextCategoryAlpha : {}", nextCategoryAlpha);
        return nextCategoryAlphaRepository.save(nextCategoryAlpha);
    }

    /**
     * Partially update a nextCategoryAlpha.
     *
     * @param nextCategoryAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryAlpha> partialUpdate(NextCategoryAlpha nextCategoryAlpha) {
        LOG.debug("Request to partially update NextCategoryAlpha : {}", nextCategoryAlpha);

        return nextCategoryAlphaRepository
            .findById(nextCategoryAlpha.getId())
            .map(existingNextCategoryAlpha -> {
                if (nextCategoryAlpha.getName() != null) {
                    existingNextCategoryAlpha.setName(nextCategoryAlpha.getName());
                }
                if (nextCategoryAlpha.getDescription() != null) {
                    existingNextCategoryAlpha.setDescription(nextCategoryAlpha.getDescription());
                }

                return existingNextCategoryAlpha;
            })
            .map(nextCategoryAlphaRepository::save);
    }

    /**
     * Get one nextCategoryAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryAlpha> findOne(Long id) {
        LOG.debug("Request to get NextCategoryAlpha : {}", id);
        return nextCategoryAlphaRepository.findById(id);
    }

    /**
     * Delete the nextCategoryAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryAlpha : {}", id);
        nextCategoryAlphaRepository.deleteById(id);
    }
}
