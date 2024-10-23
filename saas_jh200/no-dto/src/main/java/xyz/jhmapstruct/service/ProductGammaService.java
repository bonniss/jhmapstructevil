package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductGamma;
import xyz.jhmapstruct.repository.ProductGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductGamma}.
 */
@Service
@Transactional
public class ProductGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductGammaService.class);

    private final ProductGammaRepository productGammaRepository;

    public ProductGammaService(ProductGammaRepository productGammaRepository) {
        this.productGammaRepository = productGammaRepository;
    }

    /**
     * Save a productGamma.
     *
     * @param productGamma the entity to save.
     * @return the persisted entity.
     */
    public ProductGamma save(ProductGamma productGamma) {
        LOG.debug("Request to save ProductGamma : {}", productGamma);
        return productGammaRepository.save(productGamma);
    }

    /**
     * Update a productGamma.
     *
     * @param productGamma the entity to save.
     * @return the persisted entity.
     */
    public ProductGamma update(ProductGamma productGamma) {
        LOG.debug("Request to update ProductGamma : {}", productGamma);
        return productGammaRepository.save(productGamma);
    }

    /**
     * Partially update a productGamma.
     *
     * @param productGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductGamma> partialUpdate(ProductGamma productGamma) {
        LOG.debug("Request to partially update ProductGamma : {}", productGamma);

        return productGammaRepository
            .findById(productGamma.getId())
            .map(existingProductGamma -> {
                if (productGamma.getName() != null) {
                    existingProductGamma.setName(productGamma.getName());
                }
                if (productGamma.getPrice() != null) {
                    existingProductGamma.setPrice(productGamma.getPrice());
                }
                if (productGamma.getStock() != null) {
                    existingProductGamma.setStock(productGamma.getStock());
                }
                if (productGamma.getDescription() != null) {
                    existingProductGamma.setDescription(productGamma.getDescription());
                }

                return existingProductGamma;
            })
            .map(productGammaRepository::save);
    }

    /**
     * Get all the productGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductGamma> findAllWithEagerRelationships(Pageable pageable) {
        return productGammaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductGamma> findOne(Long id) {
        LOG.debug("Request to get ProductGamma : {}", id);
        return productGammaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductGamma : {}", id);
        productGammaRepository.deleteById(id);
    }
}
