package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryBeta;
import xyz.jhmapstruct.repository.CategoryBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryBeta}.
 */
@Service
@Transactional
public class CategoryBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryBetaService.class);

    private final CategoryBetaRepository categoryBetaRepository;

    public CategoryBetaService(CategoryBetaRepository categoryBetaRepository) {
        this.categoryBetaRepository = categoryBetaRepository;
    }

    /**
     * Save a categoryBeta.
     *
     * @param categoryBeta the entity to save.
     * @return the persisted entity.
     */
    public CategoryBeta save(CategoryBeta categoryBeta) {
        LOG.debug("Request to save CategoryBeta : {}", categoryBeta);
        return categoryBetaRepository.save(categoryBeta);
    }

    /**
     * Update a categoryBeta.
     *
     * @param categoryBeta the entity to save.
     * @return the persisted entity.
     */
    public CategoryBeta update(CategoryBeta categoryBeta) {
        LOG.debug("Request to update CategoryBeta : {}", categoryBeta);
        return categoryBetaRepository.save(categoryBeta);
    }

    /**
     * Partially update a categoryBeta.
     *
     * @param categoryBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryBeta> partialUpdate(CategoryBeta categoryBeta) {
        LOG.debug("Request to partially update CategoryBeta : {}", categoryBeta);

        return categoryBetaRepository
            .findById(categoryBeta.getId())
            .map(existingCategoryBeta -> {
                if (categoryBeta.getName() != null) {
                    existingCategoryBeta.setName(categoryBeta.getName());
                }
                if (categoryBeta.getDescription() != null) {
                    existingCategoryBeta.setDescription(categoryBeta.getDescription());
                }

                return existingCategoryBeta;
            })
            .map(categoryBetaRepository::save);
    }

    /**
     * Get one categoryBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryBeta> findOne(Long id) {
        LOG.debug("Request to get CategoryBeta : {}", id);
        return categoryBetaRepository.findById(id);
    }

    /**
     * Delete the categoryBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryBeta : {}", id);
        categoryBetaRepository.deleteById(id);
    }
}
