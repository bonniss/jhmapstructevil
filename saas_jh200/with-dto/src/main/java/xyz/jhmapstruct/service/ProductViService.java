package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductVi;
import xyz.jhmapstruct.repository.ProductViRepository;
import xyz.jhmapstruct.service.dto.ProductViDTO;
import xyz.jhmapstruct.service.mapper.ProductViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductVi}.
 */
@Service
@Transactional
public class ProductViService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViService.class);

    private final ProductViRepository productViRepository;

    private final ProductViMapper productViMapper;

    public ProductViService(ProductViRepository productViRepository, ProductViMapper productViMapper) {
        this.productViRepository = productViRepository;
        this.productViMapper = productViMapper;
    }

    /**
     * Save a productVi.
     *
     * @param productViDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductViDTO save(ProductViDTO productViDTO) {
        LOG.debug("Request to save ProductVi : {}", productViDTO);
        ProductVi productVi = productViMapper.toEntity(productViDTO);
        productVi = productViRepository.save(productVi);
        return productViMapper.toDto(productVi);
    }

    /**
     * Update a productVi.
     *
     * @param productViDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductViDTO update(ProductViDTO productViDTO) {
        LOG.debug("Request to update ProductVi : {}", productViDTO);
        ProductVi productVi = productViMapper.toEntity(productViDTO);
        productVi = productViRepository.save(productVi);
        return productViMapper.toDto(productVi);
    }

    /**
     * Partially update a productVi.
     *
     * @param productViDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get all the productVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productViRepository.findAllWithEagerRelationships(pageable).map(productViMapper::toDto);
    }

    /**
     * Get one productVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductViDTO> findOne(Long id) {
        LOG.debug("Request to get ProductVi : {}", id);
        return productViRepository.findOneWithEagerRelationships(id).map(productViMapper::toDto);
    }

    /**
     * Delete the productVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductVi : {}", id);
        productViRepository.deleteById(id);
    }
}
