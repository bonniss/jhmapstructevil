package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductTheta;
import xyz.jhmapstruct.repository.ProductThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductTheta}.
 */
@Service
@Transactional
public class ProductThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductThetaService.class);

    private final ProductThetaRepository productThetaRepository;

    public ProductThetaService(ProductThetaRepository productThetaRepository) {
        this.productThetaRepository = productThetaRepository;
    }

    /**
     * Save a productTheta.
     *
     * @param productTheta the entity to save.
     * @return the persisted entity.
     */
    public ProductTheta save(ProductTheta productTheta) {
        LOG.debug("Request to save ProductTheta : {}", productTheta);
        return productThetaRepository.save(productTheta);
    }

    /**
     * Update a productTheta.
     *
     * @param productTheta the entity to save.
     * @return the persisted entity.
     */
    public ProductTheta update(ProductTheta productTheta) {
        LOG.debug("Request to update ProductTheta : {}", productTheta);
        return productThetaRepository.save(productTheta);
    }

    /**
     * Partially update a productTheta.
     *
     * @param productTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductTheta> partialUpdate(ProductTheta productTheta) {
        LOG.debug("Request to partially update ProductTheta : {}", productTheta);

        return productThetaRepository
            .findById(productTheta.getId())
            .map(existingProductTheta -> {
                if (productTheta.getName() != null) {
                    existingProductTheta.setName(productTheta.getName());
                }
                if (productTheta.getPrice() != null) {
                    existingProductTheta.setPrice(productTheta.getPrice());
                }
                if (productTheta.getStock() != null) {
                    existingProductTheta.setStock(productTheta.getStock());
                }
                if (productTheta.getDescription() != null) {
                    existingProductTheta.setDescription(productTheta.getDescription());
                }

                return existingProductTheta;
            })
            .map(productThetaRepository::save);
    }

    /**
     * Get all the productThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductTheta> findAllWithEagerRelationships(Pageable pageable) {
        return productThetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductTheta> findOne(Long id) {
        LOG.debug("Request to get ProductTheta : {}", id);
        return productThetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductTheta : {}", id);
        productThetaRepository.deleteById(id);
    }
}
