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
import xyz.jhmapstruct.domain.ProductMiMi;
import xyz.jhmapstruct.repository.ProductMiMiRepository;
import xyz.jhmapstruct.service.ProductMiMiService;
import xyz.jhmapstruct.service.dto.ProductMiMiDTO;
import xyz.jhmapstruct.service.mapper.ProductMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductMiMi}.
 */
@Service
@Transactional
public class ProductMiMiServiceImpl implements ProductMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMiMiServiceImpl.class);

    private final ProductMiMiRepository productMiMiRepository;

    private final ProductMiMiMapper productMiMiMapper;

    public ProductMiMiServiceImpl(ProductMiMiRepository productMiMiRepository, ProductMiMiMapper productMiMiMapper) {
        this.productMiMiRepository = productMiMiRepository;
        this.productMiMiMapper = productMiMiMapper;
    }

    @Override
    public ProductMiMiDTO save(ProductMiMiDTO productMiMiDTO) {
        LOG.debug("Request to save ProductMiMi : {}", productMiMiDTO);
        ProductMiMi productMiMi = productMiMiMapper.toEntity(productMiMiDTO);
        productMiMi = productMiMiRepository.save(productMiMi);
        return productMiMiMapper.toDto(productMiMi);
    }

    @Override
    public ProductMiMiDTO update(ProductMiMiDTO productMiMiDTO) {
        LOG.debug("Request to update ProductMiMi : {}", productMiMiDTO);
        ProductMiMi productMiMi = productMiMiMapper.toEntity(productMiMiDTO);
        productMiMi = productMiMiRepository.save(productMiMi);
        return productMiMiMapper.toDto(productMiMi);
    }

    @Override
    public Optional<ProductMiMiDTO> partialUpdate(ProductMiMiDTO productMiMiDTO) {
        LOG.debug("Request to partially update ProductMiMi : {}", productMiMiDTO);

        return productMiMiRepository
            .findById(productMiMiDTO.getId())
            .map(existingProductMiMi -> {
                productMiMiMapper.partialUpdate(existingProductMiMi, productMiMiDTO);

                return existingProductMiMi;
            })
            .map(productMiMiRepository::save)
            .map(productMiMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductMiMiDTO> findAll() {
        LOG.debug("Request to get all ProductMiMis");
        return productMiMiRepository.findAll().stream().map(productMiMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ProductMiMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productMiMiRepository.findAllWithEagerRelationships(pageable).map(productMiMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get ProductMiMi : {}", id);
        return productMiMiRepository.findOneWithEagerRelationships(id).map(productMiMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProductMiMi : {}", id);
        productMiMiRepository.deleteById(id);
    }
}
