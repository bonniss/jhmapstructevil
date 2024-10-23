package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ProductSigma;
import xyz.jhmapstruct.repository.ProductSigmaRepository;
import xyz.jhmapstruct.service.dto.ProductSigmaDTO;
import xyz.jhmapstruct.service.mapper.ProductSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ProductSigma}.
 */
@Service
@Transactional
public class ProductSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductSigmaService.class);

    private final ProductSigmaRepository productSigmaRepository;

    private final ProductSigmaMapper productSigmaMapper;

    public ProductSigmaService(ProductSigmaRepository productSigmaRepository, ProductSigmaMapper productSigmaMapper) {
        this.productSigmaRepository = productSigmaRepository;
        this.productSigmaMapper = productSigmaMapper;
    }

    /**
     * Save a productSigma.
     *
     * @param productSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductSigmaDTO save(ProductSigmaDTO productSigmaDTO) {
        LOG.debug("Request to save ProductSigma : {}", productSigmaDTO);
        ProductSigma productSigma = productSigmaMapper.toEntity(productSigmaDTO);
        productSigma = productSigmaRepository.save(productSigma);
        return productSigmaMapper.toDto(productSigma);
    }

    /**
     * Update a productSigma.
     *
     * @param productSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductSigmaDTO update(ProductSigmaDTO productSigmaDTO) {
        LOG.debug("Request to update ProductSigma : {}", productSigmaDTO);
        ProductSigma productSigma = productSigmaMapper.toEntity(productSigmaDTO);
        productSigma = productSigmaRepository.save(productSigma);
        return productSigmaMapper.toDto(productSigma);
    }

    /**
     * Partially update a productSigma.
     *
     * @param productSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductSigmaDTO> partialUpdate(ProductSigmaDTO productSigmaDTO) {
        LOG.debug("Request to partially update ProductSigma : {}", productSigmaDTO);

        return productSigmaRepository
            .findById(productSigmaDTO.getId())
            .map(existingProductSigma -> {
                productSigmaMapper.partialUpdate(existingProductSigma, productSigmaDTO);

                return existingProductSigma;
            })
            .map(productSigmaRepository::save)
            .map(productSigmaMapper::toDto);
    }

    /**
     * Get all the productSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductSigmaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productSigmaRepository.findAllWithEagerRelationships(pageable).map(productSigmaMapper::toDto);
    }

    /**
     * Get one productSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get ProductSigma : {}", id);
        return productSigmaRepository.findOneWithEagerRelationships(id).map(productSigmaMapper::toDto);
    }

    /**
     * Delete the productSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductSigma : {}", id);
        productSigmaRepository.deleteById(id);
    }
}
