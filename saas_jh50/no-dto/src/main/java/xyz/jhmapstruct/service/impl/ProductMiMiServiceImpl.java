package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductMiMi;
import xyz.jhmapstruct.repository.ProductMiMiRepository;
import xyz.jhmapstruct.service.ProductMiMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductMiMi}.
 */
@Service
@Transactional
public class ProductMiMiServiceImpl implements ProductMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMiMiServiceImpl.class);

    private final ProductMiMiRepository productMiMiRepository;

    public ProductMiMiServiceImpl(ProductMiMiRepository productMiMiRepository) {
        this.productMiMiRepository = productMiMiRepository;
    }

    @Override
    public ProductMiMi save(ProductMiMi productMiMi) {
        LOG.debug("Request to save ProductMiMi : {}", productMiMi);
        return productMiMiRepository.save(productMiMi);
    }

    @Override
    public ProductMiMi update(ProductMiMi productMiMi) {
        LOG.debug("Request to update ProductMiMi : {}", productMiMi);
        return productMiMiRepository.save(productMiMi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<ProductMiMi> findAll() {
        LOG.debug("Request to get all ProductMiMis");
        return productMiMiRepository.findAll();
    }

    public Page<ProductMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return productMiMiRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductMiMi> findOne(Long id) {
        LOG.debug("Request to get ProductMiMi : {}", id);
        return productMiMiRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProductMiMi : {}", id);
        productMiMiRepository.deleteById(id);
    }
}
