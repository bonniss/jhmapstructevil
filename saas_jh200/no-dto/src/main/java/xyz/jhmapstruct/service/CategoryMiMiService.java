package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryMiMi;
import xyz.jhmapstruct.repository.CategoryMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryMiMi}.
 */
@Service
@Transactional
public class CategoryMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryMiMiService.class);

    private final CategoryMiMiRepository categoryMiMiRepository;

    public CategoryMiMiService(CategoryMiMiRepository categoryMiMiRepository) {
        this.categoryMiMiRepository = categoryMiMiRepository;
    }

    /**
     * Save a categoryMiMi.
     *
     * @param categoryMiMi the entity to save.
     * @return the persisted entity.
     */
    public CategoryMiMi save(CategoryMiMi categoryMiMi) {
        LOG.debug("Request to save CategoryMiMi : {}", categoryMiMi);
        return categoryMiMiRepository.save(categoryMiMi);
    }

    /**
     * Update a categoryMiMi.
     *
     * @param categoryMiMi the entity to save.
     * @return the persisted entity.
     */
    public CategoryMiMi update(CategoryMiMi categoryMiMi) {
        LOG.debug("Request to update CategoryMiMi : {}", categoryMiMi);
        return categoryMiMiRepository.save(categoryMiMi);
    }

    /**
     * Partially update a categoryMiMi.
     *
     * @param categoryMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryMiMi> partialUpdate(CategoryMiMi categoryMiMi) {
        LOG.debug("Request to partially update CategoryMiMi : {}", categoryMiMi);

        return categoryMiMiRepository
            .findById(categoryMiMi.getId())
            .map(existingCategoryMiMi -> {
                if (categoryMiMi.getName() != null) {
                    existingCategoryMiMi.setName(categoryMiMi.getName());
                }
                if (categoryMiMi.getDescription() != null) {
                    existingCategoryMiMi.setDescription(categoryMiMi.getDescription());
                }

                return existingCategoryMiMi;
            })
            .map(categoryMiMiRepository::save);
    }

    /**
     * Get one categoryMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryMiMi> findOne(Long id) {
        LOG.debug("Request to get CategoryMiMi : {}", id);
        return categoryMiMiRepository.findById(id);
    }

    /**
     * Delete the categoryMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryMiMi : {}", id);
        categoryMiMiRepository.deleteById(id);
    }
}
