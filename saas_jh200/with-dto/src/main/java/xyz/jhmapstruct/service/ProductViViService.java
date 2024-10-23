package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductViVi;
import xyz.jhmapstruct.repository.ProductViViRepository;
import xyz.jhmapstruct.service.dto.ProductViViDTO;
import xyz.jhmapstruct.service.mapper.ProductViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductViVi}.
 */
@Service
@Transactional
public class ProductViViService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViViService.class);

    private final ProductViViRepository productViViRepository;

    private final ProductViViMapper productViViMapper;

    public ProductViViService(ProductViViRepository productViViRepository, ProductViViMapper productViViMapper) {
        this.productViViRepository = productViViRepository;
        this.productViViMapper = productViViMapper;
    }

    /**
     * Save a productViVi.
     *
     * @param productViViDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductViViDTO save(ProductViViDTO productViViDTO) {
        LOG.debug("Request to save ProductViVi : {}", productViViDTO);
        ProductViVi productViVi = productViViMapper.toEntity(productViViDTO);
        productViVi = productViViRepository.save(productViVi);
        return productViViMapper.toDto(productViVi);
    }

    /**
     * Update a productViVi.
     *
     * @param productViViDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductViViDTO update(ProductViViDTO productViViDTO) {
        LOG.debug("Request to update ProductViVi : {}", productViViDTO);
        ProductViVi productViVi = productViViMapper.toEntity(productViViDTO);
        productViVi = productViViRepository.save(productViVi);
        return productViViMapper.toDto(productViVi);
    }

    /**
     * Partially update a productViVi.
     *
     * @param productViViDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get all the productViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductViViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productViViRepository.findAllWithEagerRelationships(pageable).map(productViViMapper::toDto);
    }

    /**
     * Get one productViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductViViDTO> findOne(Long id) {
        LOG.debug("Request to get ProductViVi : {}", id);
        return productViViRepository.findOneWithEagerRelationships(id).map(productViViMapper::toDto);
    }

    /**
     * Delete the productViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductViVi : {}", id);
        productViViRepository.deleteById(id);
    }
}
