package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryMi;
import xyz.jhmapstruct.repository.CategoryMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryMi}.
 */
@Service
@Transactional
public class CategoryMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryMiService.class);

    private final CategoryMiRepository categoryMiRepository;

    public CategoryMiService(CategoryMiRepository categoryMiRepository) {
        this.categoryMiRepository = categoryMiRepository;
    }

    /**
     * Save a categoryMi.
     *
     * @param categoryMi the entity to save.
     * @return the persisted entity.
     */
    public CategoryMi save(CategoryMi categoryMi) {
        LOG.debug("Request to save CategoryMi : {}", categoryMi);
        return categoryMiRepository.save(categoryMi);
    }

    /**
     * Update a categoryMi.
     *
     * @param categoryMi the entity to save.
     * @return the persisted entity.
     */
    public CategoryMi update(CategoryMi categoryMi) {
        LOG.debug("Request to update CategoryMi : {}", categoryMi);
        return categoryMiRepository.save(categoryMi);
    }

    /**
     * Partially update a categoryMi.
     *
     * @param categoryMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryMi> partialUpdate(CategoryMi categoryMi) {
        LOG.debug("Request to partially update CategoryMi : {}", categoryMi);

        return categoryMiRepository
            .findById(categoryMi.getId())
            .map(existingCategoryMi -> {
                if (categoryMi.getName() != null) {
                    existingCategoryMi.setName(categoryMi.getName());
                }
                if (categoryMi.getDescription() != null) {
                    existingCategoryMi.setDescription(categoryMi.getDescription());
                }

                return existingCategoryMi;
            })
            .map(categoryMiRepository::save);
    }

    /**
     * Get one categoryMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryMi> findOne(Long id) {
        LOG.debug("Request to get CategoryMi : {}", id);
        return categoryMiRepository.findById(id);
    }

    /**
     * Delete the categoryMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryMi : {}", id);
        categoryMiRepository.deleteById(id);
    }
}
