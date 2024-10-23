package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductBeta;
import xyz.jhmapstruct.repository.NextProductBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductBeta}.
 */
@Service
@Transactional
public class NextProductBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductBetaService.class);

    private final NextProductBetaRepository nextProductBetaRepository;

    public NextProductBetaService(NextProductBetaRepository nextProductBetaRepository) {
        this.nextProductBetaRepository = nextProductBetaRepository;
    }

    /**
     * Save a nextProductBeta.
     *
     * @param nextProductBeta the entity to save.
     * @return the persisted entity.
     */
    public NextProductBeta save(NextProductBeta nextProductBeta) {
        LOG.debug("Request to save NextProductBeta : {}", nextProductBeta);
        return nextProductBetaRepository.save(nextProductBeta);
    }

    /**
     * Update a nextProductBeta.
     *
     * @param nextProductBeta the entity to save.
     * @return the persisted entity.
     */
    public NextProductBeta update(NextProductBeta nextProductBeta) {
        LOG.debug("Request to update NextProductBeta : {}", nextProductBeta);
        return nextProductBetaRepository.save(nextProductBeta);
    }

    /**
     * Partially update a nextProductBeta.
     *
     * @param nextProductBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductBeta> partialUpdate(NextProductBeta nextProductBeta) {
        LOG.debug("Request to partially update NextProductBeta : {}", nextProductBeta);

        return nextProductBetaRepository
            .findById(nextProductBeta.getId())
            .map(existingNextProductBeta -> {
                if (nextProductBeta.getName() != null) {
                    existingNextProductBeta.setName(nextProductBeta.getName());
                }
                if (nextProductBeta.getPrice() != null) {
                    existingNextProductBeta.setPrice(nextProductBeta.getPrice());
                }
                if (nextProductBeta.getStock() != null) {
                    existingNextProductBeta.setStock(nextProductBeta.getStock());
                }
                if (nextProductBeta.getDescription() != null) {
                    existingNextProductBeta.setDescription(nextProductBeta.getDescription());
                }

                return existingNextProductBeta;
            })
            .map(nextProductBetaRepository::save);
    }

    /**
     * Get all the nextProductBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductBeta> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductBetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextProductBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductBeta> findOne(Long id) {
        LOG.debug("Request to get NextProductBeta : {}", id);
        return nextProductBetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextProductBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductBeta : {}", id);
        nextProductBetaRepository.deleteById(id);
    }
}
