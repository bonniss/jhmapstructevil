package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductVi;
import xyz.jhmapstruct.repository.ProductViRepository;
import xyz.jhmapstruct.service.ProductViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductVi}.
 */
@Service
@Transactional
public class ProductViServiceImpl implements ProductViService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViServiceImpl.class);

    private final ProductViRepository productViRepository;

    public ProductViServiceImpl(ProductViRepository productViRepository) {
        this.productViRepository = productViRepository;
    }

    @Override
    public ProductVi save(ProductVi productVi) {
        LOG.debug("Request to save ProductVi : {}", productVi);
        return productViRepository.save(productVi);
    }

    @Override
    public ProductVi update(ProductVi productVi) {
        LOG.debug("Request to update ProductVi : {}", productVi);
        return productViRepository.save(productVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<ProductVi> findAll() {
        LOG.debug("Request to get all ProductVis");
        return productViRepository.findAll();
    }

    public Page<ProductVi> findAllWithEagerRelationships(Pageable pageable) {
        return productViRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductVi> findOne(Long id) {
        LOG.debug("Request to get ProductVi : {}", id);
        return productViRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProductVi : {}", id);
        productViRepository.deleteById(id);
    }
}
