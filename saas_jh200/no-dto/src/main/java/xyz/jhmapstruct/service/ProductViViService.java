package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductViVi;
import xyz.jhmapstruct.repository.ProductViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductViVi}.
 */
@Service
@Transactional
public class ProductViViService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViViService.class);

    private final ProductViViRepository productViViRepository;

    public ProductViViService(ProductViViRepository productViViRepository) {
        this.productViViRepository = productViViRepository;
    }

    /**
     * Save a productViVi.
     *
     * @param productViVi the entity to save.
     * @return the persisted entity.
     */
    public ProductViVi save(ProductViVi productViVi) {
        LOG.debug("Request to save ProductViVi : {}", productViVi);
        return productViViRepository.save(productViVi);
    }

    /**
     * Update a productViVi.
     *
     * @param productViVi the entity to save.
     * @return the persisted entity.
     */
    public ProductViVi update(ProductViVi productViVi) {
        LOG.debug("Request to update ProductViVi : {}", productViVi);
        return productViViRepository.save(productViVi);
    }

    /**
     * Partially update a productViVi.
     *
     * @param productViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductViVi> partialUpdate(ProductViVi productViVi) {
        LOG.debug("Request to partially update ProductViVi : {}", productViVi);

        return productViViRepository
            .findById(productViVi.getId())
            .map(existingProductViVi -> {
                if (productViVi.getName() != null) {
                    existingProductViVi.setName(productViVi.getName());
                }
                if (productViVi.getPrice() != null) {
                    existingProductViVi.setPrice(productViVi.getPrice());
                }
                if (productViVi.getStock() != null) {
                    existingProductViVi.setStock(productViVi.getStock());
                }
                if (productViVi.getDescription() != null) {
                    existingProductViVi.setDescription(productViVi.getDescription());
                }

                return existingProductViVi;
            })
            .map(productViViRepository::save);
    }

    /**
     * Get all the productViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductViVi> findAllWithEagerRelationships(Pageable pageable) {
        return productViViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductViVi> findOne(Long id) {
        LOG.debug("Request to get ProductViVi : {}", id);
        return productViViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductViVi : {}", id);
        productViViRepository.deleteById(id);
    }
}
