package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductVi;
import xyz.jhmapstruct.repository.ProductViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductVi}.
 */
@Service
@Transactional
public class ProductViService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViService.class);

    private final ProductViRepository productViRepository;

    public ProductViService(ProductViRepository productViRepository) {
        this.productViRepository = productViRepository;
    }

    /**
     * Save a productVi.
     *
     * @param productVi the entity to save.
     * @return the persisted entity.
     */
    public ProductVi save(ProductVi productVi) {
        LOG.debug("Request to save ProductVi : {}", productVi);
        return productViRepository.save(productVi);
    }

    /**
     * Update a productVi.
     *
     * @param productVi the entity to save.
     * @return the persisted entity.
     */
    public ProductVi update(ProductVi productVi) {
        LOG.debug("Request to update ProductVi : {}", productVi);
        return productViRepository.save(productVi);
    }

    /**
     * Partially update a productVi.
     *
     * @param productVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductVi> partialUpdate(ProductVi productVi) {
        LOG.debug("Request to partially update ProductVi : {}", productVi);

        return productViRepository
            .findById(productVi.getId())
            .map(existingProductVi -> {
                if (productVi.getName() != null) {
                    existingProductVi.setName(productVi.getName());
                }
                if (productVi.getPrice() != null) {
                    existingProductVi.setPrice(productVi.getPrice());
                }
                if (productVi.getStock() != null) {
                    existingProductVi.setStock(productVi.getStock());
                }
                if (productVi.getDescription() != null) {
                    existingProductVi.setDescription(productVi.getDescription());
                }

                return existingProductVi;
            })
            .map(productViRepository::save);
    }

    /**
     * Get all the productVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductVi> findAllWithEagerRelationships(Pageable pageable) {
        return productViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductVi> findOne(Long id) {
        LOG.debug("Request to get ProductVi : {}", id);
        return productViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductVi : {}", id);
        productViRepository.deleteById(id);
    }
}
