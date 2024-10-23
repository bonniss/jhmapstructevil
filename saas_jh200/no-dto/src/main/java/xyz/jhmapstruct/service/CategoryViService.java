package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryVi;
import xyz.jhmapstruct.repository.CategoryViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryVi}.
 */
@Service
@Transactional
public class CategoryViService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryViService.class);

    private final CategoryViRepository categoryViRepository;

    public CategoryViService(CategoryViRepository categoryViRepository) {
        this.categoryViRepository = categoryViRepository;
    }

    /**
     * Save a categoryVi.
     *
     * @param categoryVi the entity to save.
     * @return the persisted entity.
     */
    public CategoryVi save(CategoryVi categoryVi) {
        LOG.debug("Request to save CategoryVi : {}", categoryVi);
        return categoryViRepository.save(categoryVi);
    }

    /**
     * Update a categoryVi.
     *
     * @param categoryVi the entity to save.
     * @return the persisted entity.
     */
    public CategoryVi update(CategoryVi categoryVi) {
        LOG.debug("Request to update CategoryVi : {}", categoryVi);
        return categoryViRepository.save(categoryVi);
    }

    /**
     * Partially update a categoryVi.
     *
     * @param categoryVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryVi> partialUpdate(CategoryVi categoryVi) {
        LOG.debug("Request to partially update CategoryVi : {}", categoryVi);

        return categoryViRepository
            .findById(categoryVi.getId())
            .map(existingCategoryVi -> {
                if (categoryVi.getName() != null) {
                    existingCategoryVi.setName(categoryVi.getName());
                }
                if (categoryVi.getDescription() != null) {
                    existingCategoryVi.setDescription(categoryVi.getDescription());
                }

                return existingCategoryVi;
            })
            .map(categoryViRepository::save);
    }

    /**
     * Get one categoryVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryVi> findOne(Long id) {
        LOG.debug("Request to get CategoryVi : {}", id);
        return categoryViRepository.findById(id);
    }

    /**
     * Delete the categoryVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryVi : {}", id);
        categoryViRepository.deleteById(id);
    }
}
