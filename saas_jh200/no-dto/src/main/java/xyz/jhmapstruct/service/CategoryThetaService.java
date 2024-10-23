package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategoryTheta;
import xyz.jhmapstruct.repository.CategoryThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategoryTheta}.
 */
@Service
@Transactional
public class CategoryThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryThetaService.class);

    private final CategoryThetaRepository categoryThetaRepository;

    public CategoryThetaService(CategoryThetaRepository categoryThetaRepository) {
        this.categoryThetaRepository = categoryThetaRepository;
    }

    /**
     * Save a categoryTheta.
     *
     * @param categoryTheta the entity to save.
     * @return the persisted entity.
     */
    public CategoryTheta save(CategoryTheta categoryTheta) {
        LOG.debug("Request to save CategoryTheta : {}", categoryTheta);
        return categoryThetaRepository.save(categoryTheta);
    }

    /**
     * Update a categoryTheta.
     *
     * @param categoryTheta the entity to save.
     * @return the persisted entity.
     */
    public CategoryTheta update(CategoryTheta categoryTheta) {
        LOG.debug("Request to update CategoryTheta : {}", categoryTheta);
        return categoryThetaRepository.save(categoryTheta);
    }

    /**
     * Partially update a categoryTheta.
     *
     * @param categoryTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoryTheta> partialUpdate(CategoryTheta categoryTheta) {
        LOG.debug("Request to partially update CategoryTheta : {}", categoryTheta);

        return categoryThetaRepository
            .findById(categoryTheta.getId())
            .map(existingCategoryTheta -> {
                if (categoryTheta.getName() != null) {
                    existingCategoryTheta.setName(categoryTheta.getName());
                }
                if (categoryTheta.getDescription() != null) {
                    existingCategoryTheta.setDescription(categoryTheta.getDescription());
                }

                return existingCategoryTheta;
            })
            .map(categoryThetaRepository::save);
    }

    /**
     * Get one categoryTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryTheta> findOne(Long id) {
        LOG.debug("Request to get CategoryTheta : {}", id);
        return categoryThetaRepository.findById(id);
    }

    /**
     * Delete the categoryTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategoryTheta : {}", id);
        categoryThetaRepository.deleteById(id);
    }
}
