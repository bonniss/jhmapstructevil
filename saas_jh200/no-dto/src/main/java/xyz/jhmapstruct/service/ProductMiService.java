package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.repository.ProductMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductMi}.
 */
@Service
@Transactional
public class ProductMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMiService.class);

    private final ProductMiRepository productMiRepository;

    public ProductMiService(ProductMiRepository productMiRepository) {
        this.productMiRepository = productMiRepository;
    }

    /**
     * Save a productMi.
     *
     * @param productMi the entity to save.
     * @return the persisted entity.
     */
    public ProductMi save(ProductMi productMi) {
        LOG.debug("Request to save ProductMi : {}", productMi);
        return productMiRepository.save(productMi);
    }

    /**
     * Update a productMi.
     *
     * @param productMi the entity to save.
     * @return the persisted entity.
     */
    public ProductMi update(ProductMi productMi) {
        LOG.debug("Request to update ProductMi : {}", productMi);
        return productMiRepository.save(productMi);
    }

    /**
     * Partially update a productMi.
     *
     * @param productMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductMi> partialUpdate(ProductMi productMi) {
        LOG.debug("Request to partially update ProductMi : {}", productMi);

        return productMiRepository
            .findById(productMi.getId())
            .map(existingProductMi -> {
                if (productMi.getName() != null) {
                    existingProductMi.setName(productMi.getName());
                }
                if (productMi.getPrice() != null) {
                    existingProductMi.setPrice(productMi.getPrice());
                }
                if (productMi.getStock() != null) {
                    existingProductMi.setStock(productMi.getStock());
                }
                if (productMi.getDescription() != null) {
                    existingProductMi.setDescription(productMi.getDescription());
                }

                return existingProductMi;
            })
            .map(productMiRepository::save);
    }

    /**
     * Get all the productMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductMi> findAllWithEagerRelationships(Pageable pageable) {
        return productMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductMi> findOne(Long id) {
        LOG.debug("Request to get ProductMi : {}", id);
        return productMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductMi : {}", id);
        productMiRepository.deleteById(id);
    }
}
