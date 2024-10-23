package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductSigma;
import xyz.jhmapstruct.repository.ProductSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductSigma}.
 */
@Service
@Transactional
public class ProductSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductSigmaService.class);

    private final ProductSigmaRepository productSigmaRepository;

    public ProductSigmaService(ProductSigmaRepository productSigmaRepository) {
        this.productSigmaRepository = productSigmaRepository;
    }

    /**
     * Save a productSigma.
     *
     * @param productSigma the entity to save.
     * @return the persisted entity.
     */
    public ProductSigma save(ProductSigma productSigma) {
        LOG.debug("Request to save ProductSigma : {}", productSigma);
        return productSigmaRepository.save(productSigma);
    }

    /**
     * Update a productSigma.
     *
     * @param productSigma the entity to save.
     * @return the persisted entity.
     */
    public ProductSigma update(ProductSigma productSigma) {
        LOG.debug("Request to update ProductSigma : {}", productSigma);
        return productSigmaRepository.save(productSigma);
    }

    /**
     * Partially update a productSigma.
     *
     * @param productSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductSigma> partialUpdate(ProductSigma productSigma) {
        LOG.debug("Request to partially update ProductSigma : {}", productSigma);

        return productSigmaRepository
            .findById(productSigma.getId())
            .map(existingProductSigma -> {
                if (productSigma.getName() != null) {
                    existingProductSigma.setName(productSigma.getName());
                }
                if (productSigma.getPrice() != null) {
                    existingProductSigma.setPrice(productSigma.getPrice());
                }
                if (productSigma.getStock() != null) {
                    existingProductSigma.setStock(productSigma.getStock());
                }
                if (productSigma.getDescription() != null) {
                    existingProductSigma.setDescription(productSigma.getDescription());
                }

                return existingProductSigma;
            })
            .map(productSigmaRepository::save);
    }

    /**
     * Get all the productSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductSigma> findAllWithEagerRelationships(Pageable pageable) {
        return productSigmaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductSigma> findOne(Long id) {
        LOG.debug("Request to get ProductSigma : {}", id);
        return productSigmaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductSigma : {}", id);
        productSigmaRepository.deleteById(id);
    }
}
