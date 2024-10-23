package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductGamma;
import xyz.jhmapstruct.repository.ProductGammaRepository;
import xyz.jhmapstruct.service.dto.ProductGammaDTO;
import xyz.jhmapstruct.service.mapper.ProductGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductGamma}.
 */
@Service
@Transactional
public class ProductGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductGammaService.class);

    private final ProductGammaRepository productGammaRepository;

    private final ProductGammaMapper productGammaMapper;

    public ProductGammaService(ProductGammaRepository productGammaRepository, ProductGammaMapper productGammaMapper) {
        this.productGammaRepository = productGammaRepository;
        this.productGammaMapper = productGammaMapper;
    }

    /**
     * Save a productGamma.
     *
     * @param productGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductGammaDTO save(ProductGammaDTO productGammaDTO) {
        LOG.debug("Request to save ProductGamma : {}", productGammaDTO);
        ProductGamma productGamma = productGammaMapper.toEntity(productGammaDTO);
        productGamma = productGammaRepository.save(productGamma);
        return productGammaMapper.toDto(productGamma);
    }

    /**
     * Update a productGamma.
     *
     * @param productGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductGammaDTO update(ProductGammaDTO productGammaDTO) {
        LOG.debug("Request to update ProductGamma : {}", productGammaDTO);
        ProductGamma productGamma = productGammaMapper.toEntity(productGammaDTO);
        productGamma = productGammaRepository.save(productGamma);
        return productGammaMapper.toDto(productGamma);
    }

    /**
     * Partially update a productGamma.
     *
     * @param productGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductGammaDTO> partialUpdate(ProductGammaDTO productGammaDTO) {
        LOG.debug("Request to partially update ProductGamma : {}", productGammaDTO);

        return productGammaRepository
            .findById(productGammaDTO.getId())
            .map(existingProductGamma -> {
                productGammaMapper.partialUpdate(existingProductGamma, productGammaDTO);

                return existingProductGamma;
            })
            .map(productGammaRepository::save)
            .map(productGammaMapper::toDto);
    }

    /**
     * Get all the productGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductGammaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productGammaRepository.findAllWithEagerRelationships(pageable).map(productGammaMapper::toDto);
    }

    /**
     * Get one productGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductGammaDTO> findOne(Long id) {
        LOG.debug("Request to get ProductGamma : {}", id);
        return productGammaRepository.findOneWithEagerRelationships(id).map(productGammaMapper::toDto);
    }

    /**
     * Delete the productGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductGamma : {}", id);
        productGammaRepository.deleteById(id);
    }
}
