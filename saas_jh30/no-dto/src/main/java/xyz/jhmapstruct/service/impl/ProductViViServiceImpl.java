package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductViVi;
import xyz.jhmapstruct.repository.ProductViViRepository;
import xyz.jhmapstruct.service.ProductViViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductViVi}.
 */
@Service
@Transactional
public class ProductViViServiceImpl implements ProductViViService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViViServiceImpl.class);

    private final ProductViViRepository productViViRepository;

    public ProductViViServiceImpl(ProductViViRepository productViViRepository) {
        this.productViViRepository = productViViRepository;
    }

    @Override
    public ProductViVi save(ProductViVi productViVi) {
        LOG.debug("Request to save ProductViVi : {}", productViVi);
        return productViViRepository.save(productViVi);
    }

    @Override
    public ProductViVi update(ProductViVi productViVi) {
        LOG.debug("Request to update ProductViVi : {}", productViVi);
        return productViViRepository.save(productViVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<ProductViVi> findAll() {
        LOG.debug("Request to get all ProductViVis");
        return productViViRepository.findAll();
    }

    public Page<ProductViVi> findAllWithEagerRelationships(Pageable pageable) {
        return productViViRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductViVi> findOne(Long id) {
        LOG.debug("Request to get ProductViVi : {}", id);
        return productViViRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProductViVi : {}", id);
        productViViRepository.deleteById(id);
    }
}
