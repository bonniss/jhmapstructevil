package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProduct;
import xyz.jhmapstruct.repository.NextProductRepository;
import xyz.jhmapstruct.service.dto.NextProductDTO;
import xyz.jhmapstruct.service.mapper.NextProductMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProduct}.
 */
@Service
@Transactional
public class NextProductService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductService.class);

    private final NextProductRepository nextProductRepository;

    private final NextProductMapper nextProductMapper;

    public NextProductService(NextProductRepository nextProductRepository, NextProductMapper nextProductMapper) {
        this.nextProductRepository = nextProductRepository;
        this.nextProductMapper = nextProductMapper;
    }

    /**
     * Save a nextProduct.
     *
     * @param nextProductDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductDTO save(NextProductDTO nextProductDTO) {
        LOG.debug("Request to save NextProduct : {}", nextProductDTO);
        NextProduct nextProduct = nextProductMapper.toEntity(nextProductDTO);
        nextProduct = nextProductRepository.save(nextProduct);
        return nextProductMapper.toDto(nextProduct);
    }

    /**
     * Update a nextProduct.
     *
     * @param nextProductDTO the entity to save.
     * @return the persisted entity.
     */
    public NextProductDTO update(NextProductDTO nextProductDTO) {
        LOG.debug("Request to update NextProduct : {}", nextProductDTO);
        NextProduct nextProduct = nextProductMapper.toEntity(nextProductDTO);
        nextProduct = nextProductRepository.save(nextProduct);
        return nextProductMapper.toDto(nextProduct);
    }

    /**
     * Partially update a nextProduct.
     *
     * @param nextProductDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductDTO> partialUpdate(NextProductDTO nextProductDTO) {
        LOG.debug("Request to partially update NextProduct : {}", nextProductDTO);

        return nextProductRepository
            .findById(nextProductDTO.getId())
            .map(existingNextProduct -> {
                nextProductMapper.partialUpdate(existingNextProduct, nextProductDTO);

                return existingNextProduct;
            })
            .map(nextProductRepository::save)
            .map(nextProductMapper::toDto);
    }

    /**
     * Get all the nextProducts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductRepository.findAllWithEagerRelationships(pageable).map(nextProductMapper::toDto);
    }

    /**
     * Get one nextProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductDTO> findOne(Long id) {
        LOG.debug("Request to get NextProduct : {}", id);
        return nextProductRepository.findOneWithEagerRelationships(id).map(nextProductMapper::toDto);
    }

    /**
     * Delete the nextProduct by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProduct : {}", id);
        nextProductRepository.deleteById(id);
    }
}
