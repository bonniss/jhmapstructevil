package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategory;
import xyz.jhmapstruct.repository.NextCategoryRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategory}.
 */
@Service
@Transactional
public class NextCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryService.class);

    private final NextCategoryRepository nextCategoryRepository;

    public NextCategoryService(NextCategoryRepository nextCategoryRepository) {
        this.nextCategoryRepository = nextCategoryRepository;
    }

    /**
     * Save a nextCategory.
     *
     * @param nextCategory the entity to save.
     * @return the persisted entity.
     */
    public NextCategory save(NextCategory nextCategory) {
        LOG.debug("Request to save NextCategory : {}", nextCategory);
        return nextCategoryRepository.save(nextCategory);
    }

    /**
     * Update a nextCategory.
     *
     * @param nextCategory the entity to save.
     * @return the persisted entity.
     */
    public NextCategory update(NextCategory nextCategory) {
        LOG.debug("Request to update NextCategory : {}", nextCategory);
        return nextCategoryRepository.save(nextCategory);
    }

    /**
     * Partially update a nextCategory.
     *
     * @param nextCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategory> partialUpdate(NextCategory nextCategory) {
        LOG.debug("Request to partially update NextCategory : {}", nextCategory);

        return nextCategoryRepository
            .findById(nextCategory.getId())
            .map(existingNextCategory -> {
                if (nextCategory.getName() != null) {
                    existingNextCategory.setName(nextCategory.getName());
                }
                if (nextCategory.getDescription() != null) {
                    existingNextCategory.setDescription(nextCategory.getDescription());
                }

                return existingNextCategory;
            })
            .map(nextCategoryRepository::save);
    }

    /**
     * Get one nextCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategory> findOne(Long id) {
        LOG.debug("Request to get NextCategory : {}", id);
        return nextCategoryRepository.findById(id);
    }

    /**
     * Delete the nextCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategory : {}", id);
        nextCategoryRepository.deleteById(id);
    }
}
