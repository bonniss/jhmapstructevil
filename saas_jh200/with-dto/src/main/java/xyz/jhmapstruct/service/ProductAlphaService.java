package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductAlpha;
import xyz.jhmapstruct.repository.ProductAlphaRepository;
import xyz.jhmapstruct.service.dto.ProductAlphaDTO;
import xyz.jhmapstruct.service.mapper.ProductAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductAlpha}.
 */
@Service
@Transactional
public class ProductAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAlphaService.class);

    private final ProductAlphaRepository productAlphaRepository;

    private final ProductAlphaMapper productAlphaMapper;

    public ProductAlphaService(ProductAlphaRepository productAlphaRepository, ProductAlphaMapper productAlphaMapper) {
        this.productAlphaRepository = productAlphaRepository;
        this.productAlphaMapper = productAlphaMapper;
    }

    /**
     * Save a productAlpha.
     *
     * @param productAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductAlphaDTO save(ProductAlphaDTO productAlphaDTO) {
        LOG.debug("Request to save ProductAlpha : {}", productAlphaDTO);
        ProductAlpha productAlpha = productAlphaMapper.toEntity(productAlphaDTO);
        productAlpha = productAlphaRepository.save(productAlpha);
        return productAlphaMapper.toDto(productAlpha);
    }

    /**
     * Update a productAlpha.
     *
     * @param productAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductAlphaDTO update(ProductAlphaDTO productAlphaDTO) {
        LOG.debug("Request to update ProductAlpha : {}", productAlphaDTO);
        ProductAlpha productAlpha = productAlphaMapper.toEntity(productAlphaDTO);
        productAlpha = productAlphaRepository.save(productAlpha);
        return productAlphaMapper.toDto(productAlpha);
    }

    /**
     * Partially update a productAlpha.
     *
     * @param productAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductAlphaDTO> partialUpdate(ProductAlphaDTO productAlphaDTO) {
        LOG.debug("Request to partially update ProductAlpha : {}", productAlphaDTO);

        return productAlphaRepository
            .findById(productAlphaDTO.getId())
            .map(existingProductAlpha -> {
                productAlphaMapper.partialUpdate(existingProductAlpha, productAlphaDTO);

                return existingProductAlpha;
            })
            .map(productAlphaRepository::save)
            .map(productAlphaMapper::toDto);
    }

    /**
     * Get all the productAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductAlphaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productAlphaRepository.findAllWithEagerRelationships(pageable).map(productAlphaMapper::toDto);
    }

    /**
     * Get one productAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get ProductAlpha : {}", id);
        return productAlphaRepository.findOneWithEagerRelationships(id).map(productAlphaMapper::toDto);
    }

    /**
     * Delete the productAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductAlpha : {}", id);
        productAlphaRepository.deleteById(id);
    }
}
