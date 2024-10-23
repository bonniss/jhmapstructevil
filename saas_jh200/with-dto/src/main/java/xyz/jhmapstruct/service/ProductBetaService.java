package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductBeta;
import xyz.jhmapstruct.repository.ProductBetaRepository;
import xyz.jhmapstruct.service.dto.ProductBetaDTO;
import xyz.jhmapstruct.service.mapper.ProductBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductBeta}.
 */
@Service
@Transactional
public class ProductBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductBetaService.class);

    private final ProductBetaRepository productBetaRepository;

    private final ProductBetaMapper productBetaMapper;

    public ProductBetaService(ProductBetaRepository productBetaRepository, ProductBetaMapper productBetaMapper) {
        this.productBetaRepository = productBetaRepository;
        this.productBetaMapper = productBetaMapper;
    }

    /**
     * Save a productBeta.
     *
     * @param productBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductBetaDTO save(ProductBetaDTO productBetaDTO) {
        LOG.debug("Request to save ProductBeta : {}", productBetaDTO);
        ProductBeta productBeta = productBetaMapper.toEntity(productBetaDTO);
        productBeta = productBetaRepository.save(productBeta);
        return productBetaMapper.toDto(productBeta);
    }

    /**
     * Update a productBeta.
     *
     * @param productBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductBetaDTO update(ProductBetaDTO productBetaDTO) {
        LOG.debug("Request to update ProductBeta : {}", productBetaDTO);
        ProductBeta productBeta = productBetaMapper.toEntity(productBetaDTO);
        productBeta = productBetaRepository.save(productBeta);
        return productBetaMapper.toDto(productBeta);
    }

    /**
     * Partially update a productBeta.
     *
     * @param productBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductBetaDTO> partialUpdate(ProductBetaDTO productBetaDTO) {
        LOG.debug("Request to partially update ProductBeta : {}", productBetaDTO);

        return productBetaRepository
            .findById(productBetaDTO.getId())
            .map(existingProductBeta -> {
                productBetaMapper.partialUpdate(existingProductBeta, productBetaDTO);

                return existingProductBeta;
            })
            .map(productBetaRepository::save)
            .map(productBetaMapper::toDto);
    }

    /**
     * Get all the productBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductBetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productBetaRepository.findAllWithEagerRelationships(pageable).map(productBetaMapper::toDto);
    }

    /**
     * Get one productBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductBetaDTO> findOne(Long id) {
        LOG.debug("Request to get ProductBeta : {}", id);
        return productBetaRepository.findOneWithEagerRelationships(id).map(productBetaMapper::toDto);
    }

    /**
     * Delete the productBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductBeta : {}", id);
        productBetaRepository.deleteById(id);
    }
}
