package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductGamma;
import xyz.jhmapstruct.repository.NextProductGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductGamma}.
 */
@Service
@Transactional
public class NextProductGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductGammaService.class);

    private final NextProductGammaRepository nextProductGammaRepository;

    public NextProductGammaService(NextProductGammaRepository nextProductGammaRepository) {
        this.nextProductGammaRepository = nextProductGammaRepository;
    }

    /**
     * Save a nextProductGamma.
     *
     * @param nextProductGamma the entity to save.
     * @return the persisted entity.
     */
    public NextProductGamma save(NextProductGamma nextProductGamma) {
        LOG.debug("Request to save NextProductGamma : {}", nextProductGamma);
        return nextProductGammaRepository.save(nextProductGamma);
    }

    /**
     * Update a nextProductGamma.
     *
     * @param nextProductGamma the entity to save.
     * @return the persisted entity.
     */
    public NextProductGamma update(NextProductGamma nextProductGamma) {
        LOG.debug("Request to update NextProductGamma : {}", nextProductGamma);
        return nextProductGammaRepository.save(nextProductGamma);
    }

    /**
     * Partially update a nextProductGamma.
     *
     * @param nextProductGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductGamma> partialUpdate(NextProductGamma nextProductGamma) {
        LOG.debug("Request to partially update NextProductGamma : {}", nextProductGamma);

        return nextProductGammaRepository
            .findById(nextProductGamma.getId())
            .map(existingNextProductGamma -> {
                if (nextProductGamma.getName() != null) {
                    existingNextProductGamma.setName(nextProductGamma.getName());
                }
                if (nextProductGamma.getPrice() != null) {
                    existingNextProductGamma.setPrice(nextProductGamma.getPrice());
                }
                if (nextProductGamma.getStock() != null) {
                    existingNextProductGamma.setStock(nextProductGamma.getStock());
                }
                if (nextProductGamma.getDescription() != null) {
                    existingNextProductGamma.setDescription(nextProductGamma.getDescription());
                }

                return existingNextProductGamma;
            })
            .map(nextProductGammaRepository::save);
    }

    /**
     * Get all the nextProductGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductGamma> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductGammaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextProductGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductGamma> findOne(Long id) {
        LOG.debug("Request to get NextProductGamma : {}", id);
        return nextProductGammaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextProductGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductGamma : {}", id);
        nextProductGammaRepository.deleteById(id);
    }
}
