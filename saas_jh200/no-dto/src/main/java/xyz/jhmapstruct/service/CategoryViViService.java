package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryViVi;
import xyz.jhmapstruct.repository.CategoryViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryViVi}.
 */
@Service
@Transactional
public class CategoryViViService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryViViService.class);

    private final CategoryViViRepository categoryViViRepository;

    public CategoryViViService(CategoryViViRepository categoryViViRepository) {
        this.categoryViViRepository = categoryViViRepository;
    }

    /**
     * Save a categoryViVi.
     *
     * @param categoryViVi the entity to save.
     * @return the persisted entity.
     */
    public CategoryViVi save(CategoryViVi categoryViVi) {
        LOG.debug("Request to save CategoryViVi : {}", categoryViVi);
        return categoryViViRepository.save(categoryViVi);
    }

    /**
     * Update a categoryViVi.
     *
     * @param categoryViVi the entity to save.
     * @return the persisted entity.
     */
    public CategoryViVi update(CategoryViVi categoryViVi) {
        LOG.debug("Request to update CategoryViVi : {}", categoryViVi);
        return categoryViViRepository.save(categoryViVi);
    }

    /**
     * Partially update a categoryViVi.
     *
     * @param categoryViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryViVi> partialUpdate(CategoryViVi categoryViVi) {
        LOG.debug("Request to partially update CategoryViVi : {}", categoryViVi);

        return categoryViViRepository
            .findById(categoryViVi.getId())
            .map(existingCategoryViVi -> {
                if (categoryViVi.getName() != null) {
                    existingCategoryViVi.setName(categoryViVi.getName());
                }
                if (categoryViVi.getDescription() != null) {
                    existingCategoryViVi.setDescription(categoryViVi.getDescription());
                }

                return existingCategoryViVi;
            })
            .map(categoryViViRepository::save);
    }

    /**
     * Get one categoryViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryViVi> findOne(Long id) {
        LOG.debug("Request to get CategoryViVi : {}", id);
        return categoryViViRepository.findById(id);
    }

    /**
     * Delete the categoryViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryViVi : {}", id);
        categoryViViRepository.deleteById(id);
    }
}
