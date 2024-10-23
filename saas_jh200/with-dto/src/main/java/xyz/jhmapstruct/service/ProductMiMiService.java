package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductMiMi;
import xyz.jhmapstruct.repository.ProductMiMiRepository;
import xyz.jhmapstruct.service.dto.ProductMiMiDTO;
import xyz.jhmapstruct.service.mapper.ProductMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductMiMi}.
 */
@Service
@Transactional
public class ProductMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMiMiService.class);

    private final ProductMiMiRepository productMiMiRepository;

    private final ProductMiMiMapper productMiMiMapper;

    public ProductMiMiService(ProductMiMiRepository productMiMiRepository, ProductMiMiMapper productMiMiMapper) {
        this.productMiMiRepository = productMiMiRepository;
        this.productMiMiMapper = productMiMiMapper;
    }

    /**
     * Save a productMiMi.
     *
     * @param productMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductMiMiDTO save(ProductMiMiDTO productMiMiDTO) {
        LOG.debug("Request to save ProductMiMi : {}", productMiMiDTO);
        ProductMiMi productMiMi = productMiMiMapper.toEntity(productMiMiDTO);
        productMiMi = productMiMiRepository.save(productMiMi);
        return productMiMiMapper.toDto(productMiMi);
    }

    /**
     * Update a productMiMi.
     *
     * @param productMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductMiMiDTO update(ProductMiMiDTO productMiMiDTO) {
        LOG.debug("Request to update ProductMiMi : {}", productMiMiDTO);
        ProductMiMi productMiMi = productMiMiMapper.toEntity(productMiMiDTO);
        productMiMi = productMiMiRepository.save(productMiMi);
        return productMiMiMapper.toDto(productMiMi);
    }

    /**
     * Partially update a productMiMi.
     *
     * @param productMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get all the productMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductMiMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productMiMiRepository.findAllWithEagerRelationships(pageable).map(productMiMiMapper::toDto);
    }

    /**
     * Get one productMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get ProductMiMi : {}", id);
        return productMiMiRepository.findOneWithEagerRelationships(id).map(productMiMiMapper::toDto);
    }

    /**
     * Delete the productMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductMiMi : {}", id);
        productMiMiRepository.deleteById(id);
    }
}
