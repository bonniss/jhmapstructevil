package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductTheta;
import xyz.jhmapstruct.repository.ProductThetaRepository;
import xyz.jhmapstruct.service.dto.ProductThetaDTO;
import xyz.jhmapstruct.service.mapper.ProductThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductTheta}.
 */
@Service
@Transactional
public class ProductThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductThetaService.class);

    private final ProductThetaRepository productThetaRepository;

    private final ProductThetaMapper productThetaMapper;

    public ProductThetaService(ProductThetaRepository productThetaRepository, ProductThetaMapper productThetaMapper) {
        this.productThetaRepository = productThetaRepository;
        this.productThetaMapper = productThetaMapper;
    }

    /**
     * Save a productTheta.
     *
     * @param productThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductThetaDTO save(ProductThetaDTO productThetaDTO) {
        LOG.debug("Request to save ProductTheta : {}", productThetaDTO);
        ProductTheta productTheta = productThetaMapper.toEntity(productThetaDTO);
        productTheta = productThetaRepository.save(productTheta);
        return productThetaMapper.toDto(productTheta);
    }

    /**
     * Update a productTheta.
     *
     * @param productThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductThetaDTO update(ProductThetaDTO productThetaDTO) {
        LOG.debug("Request to update ProductTheta : {}", productThetaDTO);
        ProductTheta productTheta = productThetaMapper.toEntity(productThetaDTO);
        productTheta = productThetaRepository.save(productTheta);
        return productThetaMapper.toDto(productTheta);
    }

    /**
     * Partially update a productTheta.
     *
     * @param productThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductThetaDTO> partialUpdate(ProductThetaDTO productThetaDTO) {
        LOG.debug("Request to partially update ProductTheta : {}", productThetaDTO);

        return productThetaRepository
            .findById(productThetaDTO.getId())
            .map(existingProductTheta -> {
                productThetaMapper.partialUpdate(existingProductTheta, productThetaDTO);

                return existingProductTheta;
            })
            .map(productThetaRepository::save)
            .map(productThetaMapper::toDto);
    }

    /**
     * Get all the productThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductThetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productThetaRepository.findAllWithEagerRelationships(pageable).map(productThetaMapper::toDto);
    }

    /**
     * Get one productTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductThetaDTO> findOne(Long id) {
        LOG.debug("Request to get ProductTheta : {}", id);
        return productThetaRepository.findOneWithEagerRelationships(id).map(productThetaMapper::toDto);
    }

    /**
     * Delete the productTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductTheta : {}", id);
        productThetaRepository.deleteById(id);
    }
}
