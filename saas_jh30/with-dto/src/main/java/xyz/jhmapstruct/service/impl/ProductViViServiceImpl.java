package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductViVi;
import xyz.jhmapstruct.repository.ProductViViRepository;
import xyz.jhmapstruct.service.ProductViViService;
import xyz.jhmapstruct.service.dto.ProductViViDTO;
import xyz.jhmapstruct.service.mapper.ProductViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductViVi}.
 */
@Service
@Transactional
public class ProductViViServiceImpl implements ProductViViService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViViServiceImpl.class);

    private final ProductViViRepository productViViRepository;

    private final ProductViViMapper productViViMapper;

    public ProductViViServiceImpl(ProductViViRepository productViViRepository, ProductViViMapper productViViMapper) {
        this.productViViRepository = productViViRepository;
        this.productViViMapper = productViViMapper;
    }

    @Override
    public ProductViViDTO save(ProductViViDTO productViViDTO) {
        LOG.debug("Request to save ProductViVi : {}", productViViDTO);
        ProductViVi productViVi = productViViMapper.toEntity(productViViDTO);
        productViVi = productViViRepository.save(productViVi);
        return productViViMapper.toDto(productViVi);
    }

    @Override
    public ProductViViDTO update(ProductViViDTO productViViDTO) {
        LOG.debug("Request to update ProductViVi : {}", productViViDTO);
        ProductViVi productViVi = productViViMapper.toEntity(productViViDTO);
        productViVi = productViViRepository.save(productViVi);
        return productViViMapper.toDto(productViVi);
    }

    @Override
    public Optional<ProductViViDTO> partialUpdate(ProductViViDTO productViViDTO) {
        LOG.debug("Request to partially update ProductViVi : {}", productViViDTO);

        return productViViRepository
            .findById(productViViDTO.getId())
            .map(existingProductViVi -> {
                productViViMapper.partialUpdate(existingProductViVi, productViViDTO);

                return existingProductViVi;
            })
            .map(productViViRepository::save)
            .map(productViViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductViViDTO> findAll() {
        LOG.debug("Request to get all ProductViVis");
        return productViViRepository.findAll().stream().map(productViViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ProductViViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productViViRepository.findAllWithEagerRelationships(pageable).map(productViViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductViViDTO> findOne(Long id) {
        LOG.debug("Request to get ProductViVi : {}", id);
        return productViViRepository.findOneWithEagerRelationships(id).map(productViViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProductViVi : {}", id);
        productViViRepository.deleteById(id);
    }
}
