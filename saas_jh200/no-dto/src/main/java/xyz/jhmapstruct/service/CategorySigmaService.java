package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CategorySigma;
import xyz.jhmapstruct.repository.CategorySigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CategorySigma}.
 */
@Service
@Transactional
public class CategorySigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(CategorySigmaService.class);

    private final CategorySigmaRepository categorySigmaRepository;

    public CategorySigmaService(CategorySigmaRepository categorySigmaRepository) {
        this.categorySigmaRepository = categorySigmaRepository;
    }

    /**
     * Save a categorySigma.
     *
     * @param categorySigma the entity to save.
     * @return the persisted entity.
     */
    public CategorySigma save(CategorySigma categorySigma) {
        LOG.debug("Request to save CategorySigma : {}", categorySigma);
        return categorySigmaRepository.save(categorySigma);
    }

    /**
     * Update a categorySigma.
     *
     * @param categorySigma the entity to save.
     * @return the persisted entity.
     */
    public CategorySigma update(CategorySigma categorySigma) {
        LOG.debug("Request to update CategorySigma : {}", categorySigma);
        return categorySigmaRepository.save(categorySigma);
    }

    /**
     * Partially update a categorySigma.
     *
     * @param categorySigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategorySigma> partialUpdate(CategorySigma categorySigma) {
        LOG.debug("Request to partially update CategorySigma : {}", categorySigma);

        return categorySigmaRepository
            .findById(categorySigma.getId())
            .map(existingCategorySigma -> {
                if (categorySigma.getName() != null) {
                    existingCategorySigma.setName(categorySigma.getName());
                }
                if (categorySigma.getDescription() != null) {
                    existingCategorySigma.setDescription(categorySigma.getDescription());
                }

                return existingCategorySigma;
            })
            .map(categorySigmaRepository::save);
    }

    /**
     * Get one categorySigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategorySigma> findOne(Long id) {
        LOG.debug("Request to get CategorySigma : {}", id);
        return categorySigmaRepository.findById(id);
    }

    /**
     * Delete the categorySigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CategorySigma : {}", id);
        categorySigmaRepository.deleteById(id);
    }
}
