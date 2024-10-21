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
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.repository.ProductMiRepository;
import xyz.jhmapstruct.service.ProductMiService;
import xyz.jhmapstruct.service.dto.ProductMiDTO;
import xyz.jhmapstruct.service.mapper.ProductMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductMi}.
 */
@Service
@Transactional
public class ProductMiServiceImpl implements ProductMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMiServiceImpl.class);

    private final ProductMiRepository productMiRepository;

    private final ProductMiMapper productMiMapper;

    public ProductMiServiceImpl(ProductMiRepository productMiRepository, ProductMiMapper productMiMapper) {
        this.productMiRepository = productMiRepository;
        this.productMiMapper = productMiMapper;
    }

    @Override
    public ProductMiDTO save(ProductMiDTO productMiDTO) {
        LOG.debug("Request to save ProductMi : {}", productMiDTO);
        ProductMi productMi = productMiMapper.toEntity(productMiDTO);
        productMi = productMiRepository.save(productMi);
        return productMiMapper.toDto(productMi);
    }

    @Override
    public ProductMiDTO update(ProductMiDTO productMiDTO) {
        LOG.debug("Request to update ProductMi : {}", productMiDTO);
        ProductMi productMi = productMiMapper.toEntity(productMiDTO);
        productMi = productMiRepository.save(productMi);
        return productMiMapper.toDto(productMi);
    }

    @Override
    public Optional<ProductMiDTO> partialUpdate(ProductMiDTO productMiDTO) {
        LOG.debug("Request to partially update ProductMi : {}", productMiDTO);

        return productMiRepository
            .findById(productMiDTO.getId())
            .map(existingProductMi -> {
                productMiMapper.partialUpdate(existingProductMi, productMiDTO);

                return existingProductMi;
            })
            .map(productMiRepository::save)
            .map(productMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductMiDTO> findAll() {
        LOG.debug("Request to get all ProductMis");
        return productMiRepository.findAll().stream().map(productMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ProductMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productMiRepository.findAllWithEagerRelationships(pageable).map(productMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductMiDTO> findOne(Long id) {
        LOG.debug("Request to get ProductMi : {}", id);
        return productMiRepository.findOneWithEagerRelationships(id).map(productMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProductMi : {}", id);
        productMiRepository.deleteById(id);
    }
}
