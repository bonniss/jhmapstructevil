package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductSigma;
import xyz.jhmapstruct.repository.NextProductSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductSigma}.
 */
@Service
@Transactional
public class NextProductSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductSigmaService.class);

    private final NextProductSigmaRepository nextProductSigmaRepository;

    public NextProductSigmaService(NextProductSigmaRepository nextProductSigmaRepository) {
        this.nextProductSigmaRepository = nextProductSigmaRepository;
    }

    /**
     * Save a nextProductSigma.
     *
     * @param nextProductSigma the entity to save.
     * @return the persisted entity.
     */
    public NextProductSigma save(NextProductSigma nextProductSigma) {
        LOG.debug("Request to save NextProductSigma : {}", nextProductSigma);
        return nextProductSigmaRepository.save(nextProductSigma);
    }

    /**
     * Update a nextProductSigma.
     *
     * @param nextProductSigma the entity to save.
     * @return the persisted entity.
     */
    public NextProductSigma update(NextProductSigma nextProductSigma) {
        LOG.debug("Request to update NextProductSigma : {}", nextProductSigma);
        return nextProductSigmaRepository.save(nextProductSigma);
    }

    /**
     * Partially update a nextProductSigma.
     *
     * @param nextProductSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductSigma> partialUpdate(NextProductSigma nextProductSigma) {
        LOG.debug("Request to partially update NextProductSigma : {}", nextProductSigma);

        return nextProductSigmaRepository
            .findById(nextProductSigma.getId())
            .map(existingNextProductSigma -> {
                if (nextProductSigma.getName() != null) {
                    existingNextProductSigma.setName(nextProductSigma.getName());
                }
                if (nextProductSigma.getPrice() != null) {
                    existingNextProductSigma.setPrice(nextProductSigma.getPrice());
                }
                if (nextProductSigma.getStock() != null) {
                    existingNextProductSigma.setStock(nextProductSigma.getStock());
                }
                if (nextProductSigma.getDescription() != null) {
                    existingNextProductSigma.setDescription(nextProductSigma.getDescription());
                }

                return existingNextProductSigma;
            })
            .map(nextProductSigmaRepository::save);
    }

    /**
     * Get all the nextProductSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductSigma> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductSigmaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextProductSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductSigma> findOne(Long id) {
        LOG.debug("Request to get NextProductSigma : {}", id);
        return nextProductSigmaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextProductSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductSigma : {}", id);
        nextProductSigmaRepository.deleteById(id);
    }
}
