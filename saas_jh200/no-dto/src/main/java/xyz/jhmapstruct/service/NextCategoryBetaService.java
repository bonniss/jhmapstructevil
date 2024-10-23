package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryBeta;
import xyz.jhmapstruct.repository.NextCategoryBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryBeta}.
 */
@Service
@Transactional
public class NextCategoryBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryBetaService.class);

    private final NextCategoryBetaRepository nextCategoryBetaRepository;

    public NextCategoryBetaService(NextCategoryBetaRepository nextCategoryBetaRepository) {
        this.nextCategoryBetaRepository = nextCategoryBetaRepository;
    }

    /**
     * Save a nextCategoryBeta.
     *
     * @param nextCategoryBeta the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryBeta save(NextCategoryBeta nextCategoryBeta) {
        LOG.debug("Request to save NextCategoryBeta : {}", nextCategoryBeta);
        return nextCategoryBetaRepository.save(nextCategoryBeta);
    }

    /**
     * Update a nextCategoryBeta.
     *
     * @param nextCategoryBeta the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryBeta update(NextCategoryBeta nextCategoryBeta) {
        LOG.debug("Request to update NextCategoryBeta : {}", nextCategoryBeta);
        return nextCategoryBetaRepository.save(nextCategoryBeta);
    }

    /**
     * Partially update a nextCategoryBeta.
     *
     * @param nextCategoryBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryBeta> partialUpdate(NextCategoryBeta nextCategoryBeta) {
        LOG.debug("Request to partially update NextCategoryBeta : {}", nextCategoryBeta);

        return nextCategoryBetaRepository
            .findById(nextCategoryBeta.getId())
            .map(existingNextCategoryBeta -> {
                if (nextCategoryBeta.getName() != null) {
                    existingNextCategoryBeta.setName(nextCategoryBeta.getName());
                }
                if (nextCategoryBeta.getDescription() != null) {
                    existingNextCategoryBeta.setDescription(nextCategoryBeta.getDescription());
                }

                return existingNextCategoryBeta;
            })
            .map(nextCategoryBetaRepository::save);
    }

    /**
     * Get one nextCategoryBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryBeta> findOne(Long id) {
        LOG.debug("Request to get NextCategoryBeta : {}", id);
        return nextCategoryBetaRepository.findById(id);
    }

    /**
     * Delete the nextCategoryBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryBeta : {}", id);
        nextCategoryBetaRepository.deleteById(id);
    }
}
