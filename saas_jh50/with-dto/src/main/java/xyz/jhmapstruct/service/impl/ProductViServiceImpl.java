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
import xyz.jhmapstruct.domain.ProductVi;
import xyz.jhmapstruct.repository.ProductViRepository;
import xyz.jhmapstruct.service.ProductViService;
import xyz.jhmapstruct.service.dto.ProductViDTO;
import xyz.jhmapstruct.service.mapper.ProductViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductVi}.
 */
@Service
@Transactional
public class ProductViServiceImpl implements ProductViService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViServiceImpl.class);

    private final ProductViRepository productViRepository;

    private final ProductViMapper productViMapper;

    public ProductViServiceImpl(ProductViRepository productViRepository, ProductViMapper productViMapper) {
        this.productViRepository = productViRepository;
        this.productViMapper = productViMapper;
    }

    @Override
    public ProductViDTO save(ProductViDTO productViDTO) {
        LOG.debug("Request to save ProductVi : {}", productViDTO);
        ProductVi productVi = productViMapper.toEntity(productViDTO);
        productVi = productViRepository.save(productVi);
        return productViMapper.toDto(productVi);
    }

    @Override
    public ProductViDTO update(ProductViDTO productViDTO) {
        LOG.debug("Request to update ProductVi : {}", productViDTO);
        ProductVi productVi = productViMapper.toEntity(productViDTO);
        productVi = productViRepository.save(productVi);
        return productViMapper.toDto(productVi);
    }

    @Override
    public Optional<ProductViDTO> partialUpdate(ProductViDTO productViDTO) {
        LOG.debug("Request to partially update ProductVi : {}", productViDTO);

        return productViRepository
            .findById(productViDTO.getId())
            .map(existingProductVi -> {
                productViMapper.partialUpdate(existingProductVi, productViDTO);

                return existingProductVi;
            })
            .map(productViRepository::save)
            .map(productViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductViDTO> findAll() {
        LOG.debug("Request to get all ProductVis");
        return productViRepository.findAll().stream().map(productViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ProductViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productViRepository.findAllWithEagerRelationships(pageable).map(productViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductViDTO> findOne(Long id) {
        LOG.debug("Request to get ProductVi : {}", id);
        return productViRepository.findOneWithEagerRelationships(id).map(productViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProductVi : {}", id);
        productViRepository.deleteById(id);
    }
}
