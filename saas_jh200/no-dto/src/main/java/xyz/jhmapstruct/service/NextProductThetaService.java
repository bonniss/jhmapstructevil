package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextProductTheta;
import xyz.jhmapstruct.repository.NextProductThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextProductTheta}.
 */
@Service
@Transactional
public class NextProductThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductThetaService.class);

    private final NextProductThetaRepository nextProductThetaRepository;

    public NextProductThetaService(NextProductThetaRepository nextProductThetaRepository) {
        this.nextProductThetaRepository = nextProductThetaRepository;
    }

    /**
     * Save a nextProductTheta.
     *
     * @param nextProductTheta the entity to save.
     * @return the persisted entity.
     */
    public NextProductTheta save(NextProductTheta nextProductTheta) {
        LOG.debug("Request to save NextProductTheta : {}", nextProductTheta);
        return nextProductThetaRepository.save(nextProductTheta);
    }

    /**
     * Update a nextProductTheta.
     *
     * @param nextProductTheta the entity to save.
     * @return the persisted entity.
     */
    public NextProductTheta update(NextProductTheta nextProductTheta) {
        LOG.debug("Request to update NextProductTheta : {}", nextProductTheta);
        return nextProductThetaRepository.save(nextProductTheta);
    }

    /**
     * Partially update a nextProductTheta.
     *
     * @param nextProductTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextProductTheta> partialUpdate(NextProductTheta nextProductTheta) {
        LOG.debug("Request to partially update NextProductTheta : {}", nextProductTheta);

        return nextProductThetaRepository
            .findById(nextProductTheta.getId())
            .map(existingNextProductTheta -> {
                if (nextProductTheta.getName() != null) {
                    existingNextProductTheta.setName(nextProductTheta.getName());
                }
                if (nextProductTheta.getPrice() != null) {
                    existingNextProductTheta.setPrice(nextProductTheta.getPrice());
                }
                if (nextProductTheta.getStock() != null) {
                    existingNextProductTheta.setStock(nextProductTheta.getStock());
                }
                if (nextProductTheta.getDescription() != null) {
                    existingNextProductTheta.setDescription(nextProductTheta.getDescription());
                }

                return existingNextProductTheta;
            })
            .map(nextProductThetaRepository::save);
    }

    /**
     * Get all the nextProductThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextProductTheta> findAllWithEagerRelationships(Pageable pageable) {
        return nextProductThetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextProductTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextProductTheta> findOne(Long id) {
        LOG.debug("Request to get NextProductTheta : {}", id);
        return nextProductThetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextProductTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextProductTheta : {}", id);
        nextProductThetaRepository.deleteById(id);
    }
}
