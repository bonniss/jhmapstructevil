package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.repository.ProductMiRepository;
import xyz.jhmapstruct.service.ProductMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductMi}.
 */
@Service
@Transactional
public class ProductMiServiceImpl implements ProductMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMiServiceImpl.class);

    private final ProductMiRepository productMiRepository;

    public ProductMiServiceImpl(ProductMiRepository productMiRepository) {
        this.productMiRepository = productMiRepository;
    }

    @Override
    public ProductMi save(ProductMi productMi) {
        LOG.debug("Request to save ProductMi : {}", productMi);
        return productMiRepository.save(productMi);
    }

    @Override
    public ProductMi update(ProductMi productMi) {
        LOG.debug("Request to update ProductMi : {}", productMi);
        return productMiRepository.save(productMi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<ProductMi> findAll() {
        LOG.debug("Request to get all ProductMis");
        return productMiRepository.findAll();
    }

    public Page<ProductMi> findAllWithEagerRelationships(Pageable pageable) {
        return productMiRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductMi> findOne(Long id) {
        LOG.debug("Request to get ProductMi : {}", id);
        return productMiRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProductMi : {}", id);
        productMiRepository.deleteById(id);
    }
}
