package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.repository.ProductMiRepository;
import xyz.jhmapstruct.service.dto.ProductMiDTO;
import xyz.jhmapstruct.service.mapper.ProductMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductMi}.
 */
@Service
@Transactional
public class ProductMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMiService.class);

    private final ProductMiRepository productMiRepository;

    private final ProductMiMapper productMiMapper;

    public ProductMiService(ProductMiRepository productMiRepository, ProductMiMapper productMiMapper) {
        this.productMiRepository = productMiRepository;
        this.productMiMapper = productMiMapper;
    }

    /**
     * Save a productMi.
     *
     * @param productMiDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductMiDTO save(ProductMiDTO productMiDTO) {
        LOG.debug("Request to save ProductMi : {}", productMiDTO);
        ProductMi productMi = productMiMapper.toEntity(productMiDTO);
        productMi = productMiRepository.save(productMi);
        return productMiMapper.toDto(productMi);
    }

    /**
     * Update a productMi.
     *
     * @param productMiDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductMiDTO update(ProductMiDTO productMiDTO) {
        LOG.debug("Request to update ProductMi : {}", productMiDTO);
        ProductMi productMi = productMiMapper.toEntity(productMiDTO);
        productMi = productMiRepository.save(productMi);
        return productMiMapper.toDto(productMi);
    }

    /**
     * Partially update a productMi.
     *
     * @param productMiDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get all the productMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productMiRepository.findAllWithEagerRelationships(pageable).map(productMiMapper::toDto);
    }

    /**
     * Get one productMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductMiDTO> findOne(Long id) {
        LOG.debug("Request to get ProductMi : {}", id);
        return productMiRepository.findOneWithEagerRelationships(id).map(productMiMapper::toDto);
    }

    /**
     * Delete the productMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductMi : {}", id);
        productMiRepository.deleteById(id);
    }
}
