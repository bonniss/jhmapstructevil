package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryAlpha;
import xyz.jhmapstruct.repository.CategoryAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryAlpha}.
 */
@Service
@Transactional
public class CategoryAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryAlphaService.class);

    private final CategoryAlphaRepository categoryAlphaRepository;

    public CategoryAlphaService(CategoryAlphaRepository categoryAlphaRepository) {
        this.categoryAlphaRepository = categoryAlphaRepository;
    }

    /**
     * Save a categoryAlpha.
     *
     * @param categoryAlpha the entity to save.
     * @return the persisted entity.
     */
    public CategoryAlpha save(CategoryAlpha categoryAlpha) {
        LOG.debug("Request to save CategoryAlpha : {}", categoryAlpha);
        return categoryAlphaRepository.save(categoryAlpha);
    }

    /**
     * Update a categoryAlpha.
     *
     * @param categoryAlpha the entity to save.
     * @return the persisted entity.
     */
    public CategoryAlpha update(CategoryAlpha categoryAlpha) {
        LOG.debug("Request to update CategoryAlpha : {}", categoryAlpha);
        return categoryAlphaRepository.save(categoryAlpha);
    }

    /**
     * Partially update a categoryAlpha.
     *
     * @param categoryAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryAlpha> partialUpdate(CategoryAlpha categoryAlpha) {
        LOG.debug("Request to partially update CategoryAlpha : {}", categoryAlpha);

        return categoryAlphaRepository
            .findById(categoryAlpha.getId())
            .map(existingCategoryAlpha -> {
                if (categoryAlpha.getName() != null) {
                    existingCategoryAlpha.setName(categoryAlpha.getName());
                }
                if (categoryAlpha.getDescription() != null) {
                    existingCategoryAlpha.setDescription(categoryAlpha.getDescription());
                }

                return existingCategoryAlpha;
            })
            .map(categoryAlphaRepository::save);
    }

    /**
     * Get one categoryAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryAlpha> findOne(Long id) {
        LOG.debug("Request to get CategoryAlpha : {}", id);
        return categoryAlphaRepository.findById(id);
    }

    /**
     * Delete the categoryAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryAlpha : {}", id);
        categoryAlphaRepository.deleteById(id);
    }
}
