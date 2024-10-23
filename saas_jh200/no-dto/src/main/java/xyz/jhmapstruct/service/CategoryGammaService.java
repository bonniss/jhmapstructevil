package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryGamma;
import xyz.jhmapstruct.repository.CategoryGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryGamma}.
 */
@Service
@Transactional
public class CategoryGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryGammaService.class);

    private final CategoryGammaRepository categoryGammaRepository;

    public CategoryGammaService(CategoryGammaRepository categoryGammaRepository) {
        this.categoryGammaRepository = categoryGammaRepository;
    }

    /**
     * Save a categoryGamma.
     *
     * @param categoryGamma the entity to save.
     * @return the persisted entity.
     */
    public CategoryGamma save(CategoryGamma categoryGamma) {
        LOG.debug("Request to save CategoryGamma : {}", categoryGamma);
        return categoryGammaRepository.save(categoryGamma);
    }

    /**
     * Update a categoryGamma.
     *
     * @param categoryGamma the entity to save.
     * @return the persisted entity.
     */
    public CategoryGamma update(CategoryGamma categoryGamma) {
        LOG.debug("Request to update CategoryGamma : {}", categoryGamma);
        return categoryGammaRepository.save(categoryGamma);
    }

    /**
     * Partially update a categoryGamma.
     *
     * @param categoryGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryGamma> partialUpdate(CategoryGamma categoryGamma) {
        LOG.debug("Request to partially update CategoryGamma : {}", categoryGamma);

        return categoryGammaRepository
            .findById(categoryGamma.getId())
            .map(existingCategoryGamma -> {
                if (categoryGamma.getName() != null) {
                    existingCategoryGamma.setName(categoryGamma.getName());
                }
                if (categoryGamma.getDescription() != null) {
                    existingCategoryGamma.setDescription(categoryGamma.getDescription());
                }

                return existingCategoryGamma;
            })
            .map(categoryGammaRepository::save);
    }

    /**
     * Get one categoryGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryGamma> findOne(Long id) {
        LOG.debug("Request to get CategoryGamma : {}", id);
        return categoryGammaRepository.findById(id);
    }

    /**
     * Delete the categoryGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryGamma : {}", id);
        categoryGammaRepository.deleteById(id);
    }
}
