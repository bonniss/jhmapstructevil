package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductBeta;
import xyz.jhmapstruct.repository.ProductBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductBeta}.
 */
@Service
@Transactional
public class ProductBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductBetaService.class);

    private final ProductBetaRepository productBetaRepository;

    public ProductBetaService(ProductBetaRepository productBetaRepository) {
        this.productBetaRepository = productBetaRepository;
    }

    /**
     * Save a productBeta.
     *
     * @param productBeta the entity to save.
     * @return the persisted entity.
     */
    public ProductBeta save(ProductBeta productBeta) {
        LOG.debug("Request to save ProductBeta : {}", productBeta);
        return productBetaRepository.save(productBeta);
    }

    /**
     * Update a productBeta.
     *
     * @param productBeta the entity to save.
     * @return the persisted entity.
     */
    public ProductBeta update(ProductBeta productBeta) {
        LOG.debug("Request to update ProductBeta : {}", productBeta);
        return productBetaRepository.save(productBeta);
    }

    /**
     * Partially update a productBeta.
     *
     * @param productBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductBeta> partialUpdate(ProductBeta productBeta) {
        LOG.debug("Request to partially update ProductBeta : {}", productBeta);

        return productBetaRepository
            .findById(productBeta.getId())
            .map(existingProductBeta -> {
                if (productBeta.getName() != null) {
                    existingProductBeta.setName(productBeta.getName());
                }
                if (productBeta.getPrice() != null) {
                    existingProductBeta.setPrice(productBeta.getPrice());
                }
                if (productBeta.getStock() != null) {
                    existingProductBeta.setStock(productBeta.getStock());
                }
                if (productBeta.getDescription() != null) {
                    existingProductBeta.setDescription(productBeta.getDescription());
                }

                return existingProductBeta;
            })
            .map(productBetaRepository::save);
    }

    /**
     * Get all the productBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductBeta> findAllWithEagerRelationships(Pageable pageable) {
        return productBetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductBeta> findOne(Long id) {
        LOG.debug("Request to get ProductBeta : {}", id);
        return productBetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductBeta : {}", id);
        productBetaRepository.deleteById(id);
    }
}
