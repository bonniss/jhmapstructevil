package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductMiMi;
import xyz.jhmapstruct.repository.ProductMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductMiMi}.
 */
@Service
@Transactional
public class ProductMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMiMiService.class);

    private final ProductMiMiRepository productMiMiRepository;

    public ProductMiMiService(ProductMiMiRepository productMiMiRepository) {
        this.productMiMiRepository = productMiMiRepository;
    }

    /**
     * Save a productMiMi.
     *
     * @param productMiMi the entity to save.
     * @return the persisted entity.
     */
    public ProductMiMi save(ProductMiMi productMiMi) {
        LOG.debug("Request to save ProductMiMi : {}", productMiMi);
        return productMiMiRepository.save(productMiMi);
    }

    /**
     * Update a productMiMi.
     *
     * @param productMiMi the entity to save.
     * @return the persisted entity.
     */
    public ProductMiMi update(ProductMiMi productMiMi) {
        LOG.debug("Request to update ProductMiMi : {}", productMiMi);
        return productMiMiRepository.save(productMiMi);
    }

    /**
     * Partially update a productMiMi.
     *
     * @param productMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductMiMi> partialUpdate(ProductMiMi productMiMi) {
        LOG.debug("Request to partially update ProductMiMi : {}", productMiMi);

        return productMiMiRepository
            .findById(productMiMi.getId())
            .map(existingProductMiMi -> {
                if (productMiMi.getName() != null) {
                    existingProductMiMi.setName(productMiMi.getName());
                }
                if (productMiMi.getPrice() != null) {
                    existingProductMiMi.setPrice(productMiMi.getPrice());
                }
                if (productMiMi.getStock() != null) {
                    existingProductMiMi.setStock(productMiMi.getStock());
                }
                if (productMiMi.getDescription() != null) {
                    existingProductMiMi.setDescription(productMiMi.getDescription());
                }

                return existingProductMiMi;
            })
            .map(productMiMiRepository::save);
    }

    /**
     * Get all the productMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return productMiMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductMiMi> findOne(Long id) {
        LOG.debug("Request to get ProductMiMi : {}", id);
        return productMiMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductMiMi : {}", id);
        productMiMiRepository.deleteById(id);
    }
}
