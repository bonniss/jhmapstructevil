package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductAlpha;
import xyz.jhmapstruct.repository.ProductAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductAlpha}.
 */
@Service
@Transactional
public class ProductAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAlphaService.class);

    private final ProductAlphaRepository productAlphaRepository;

    public ProductAlphaService(ProductAlphaRepository productAlphaRepository) {
        this.productAlphaRepository = productAlphaRepository;
    }

    /**
     * Save a productAlpha.
     *
     * @param productAlpha the entity to save.
     * @return the persisted entity.
     */
    public ProductAlpha save(ProductAlpha productAlpha) {
        LOG.debug("Request to save ProductAlpha : {}", productAlpha);
        return productAlphaRepository.save(productAlpha);
    }

    /**
     * Update a productAlpha.
     *
     * @param productAlpha the entity to save.
     * @return the persisted entity.
     */
    public ProductAlpha update(ProductAlpha productAlpha) {
        LOG.debug("Request to update ProductAlpha : {}", productAlpha);
        return productAlphaRepository.save(productAlpha);
    }

    /**
     * Partially update a productAlpha.
     *
     * @param productAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductAlpha> partialUpdate(ProductAlpha productAlpha) {
        LOG.debug("Request to partially update ProductAlpha : {}", productAlpha);

        return productAlphaRepository
            .findById(productAlpha.getId())
            .map(existingProductAlpha -> {
                if (productAlpha.getName() != null) {
                    existingProductAlpha.setName(productAlpha.getName());
                }
                if (productAlpha.getPrice() != null) {
                    existingProductAlpha.setPrice(productAlpha.getPrice());
                }
                if (productAlpha.getStock() != null) {
                    existingProductAlpha.setStock(productAlpha.getStock());
                }
                if (productAlpha.getDescription() != null) {
                    existingProductAlpha.setDescription(productAlpha.getDescription());
                }

                return existingProductAlpha;
            })
            .map(productAlphaRepository::save);
    }

    /**
     * Get all the productAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return productAlphaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductAlpha> findOne(Long id) {
        LOG.debug("Request to get ProductAlpha : {}", id);
        return productAlphaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductAlpha : {}", id);
        productAlphaRepository.deleteById(id);
    }
}
