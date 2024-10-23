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

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProduct}.
 */
@Service
@Transactional
public class NextProductService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductService.class);

    private final NextProductRepository nextProductRepository;

    public NextProductService(NextProductRepository nextProductRepository) {
        this.nextProductRepository = nextProductRepository;
    }

    /**
     * Save a nextProduct.
     *
     * @param nextProduct the entity to save.
     * @return the persisted entity.
     */
    public NextProduct save(NextProduct nextProduct) {
        LOG.debug("Request to save NextProduct : {}", nextProduct);
        return nextProductRepository.save(nextProduct);
    }

    /**
     * Update a nextProduct.
     *
     * @param nextProduct the entity to save.
     * @return the persisted entity.
     */
    public NextProduct update(NextProduct nextProduct) {
        LOG.debug("Request to update NextProduct : {}", nextProduct);
        return nextProductRepository.save(nextProduct);
    }

    /**
     * Partially update a nextProduct.
     *
     * @param nextProduct the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProduct> partialUpdate(NextProduct nextProduct) {
        LOG.debug("Request to partially update NextProduct : {}", nextProduct);

        return nextProductRepository
            .findById(nextProduct.getId())
            .map(existingNextProduct -> {
                if (nextProduct.getName() != null) {
                    existingNextProduct.setName(nextProduct.getName());
                }
                if (nextProduct.getPrice() != null) {
                    existingNextProduct.setPrice(nextProduct.getPrice());
                }
                if (nextProduct.getStock() != null) {
                    existingNextProduct.setStock(nextProduct.getStock());
                }
                if (nextProduct.getDescription() != null) {
                    existingNextProduct.setDescription(nextProduct.getDescription());
                }

                return existingNextProduct;
            })
            .map(nextProductRepository::save);
    }

    /**
     * Get all the nextProducts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProduct> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProduct> findOne(Long id) {
        LOG.debug("Request to get NextProduct : {}", id);
        return nextProductRepository.findOneWithEagerRelationships(id);
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
