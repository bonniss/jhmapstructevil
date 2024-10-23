package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryGamma;
import xyz.jhmapstruct.repository.NextCategoryGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryGamma}.
 */
@Service
@Transactional
public class NextCategoryGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryGammaService.class);

    private final NextCategoryGammaRepository nextCategoryGammaRepository;

    public NextCategoryGammaService(NextCategoryGammaRepository nextCategoryGammaRepository) {
        this.nextCategoryGammaRepository = nextCategoryGammaRepository;
    }

    /**
     * Save a nextCategoryGamma.
     *
     * @param nextCategoryGamma the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryGamma save(NextCategoryGamma nextCategoryGamma) {
        LOG.debug("Request to save NextCategoryGamma : {}", nextCategoryGamma);
        return nextCategoryGammaRepository.save(nextCategoryGamma);
    }

    /**
     * Update a nextCategoryGamma.
     *
     * @param nextCategoryGamma the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryGamma update(NextCategoryGamma nextCategoryGamma) {
        LOG.debug("Request to update NextCategoryGamma : {}", nextCategoryGamma);
        return nextCategoryGammaRepository.save(nextCategoryGamma);
    }

    /**
     * Partially update a nextCategoryGamma.
     *
     * @param nextCategoryGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryGamma> partialUpdate(NextCategoryGamma nextCategoryGamma) {
        LOG.debug("Request to partially update NextCategoryGamma : {}", nextCategoryGamma);

        return nextCategoryGammaRepository
            .findById(nextCategoryGamma.getId())
            .map(existingNextCategoryGamma -> {
                if (nextCategoryGamma.getName() != null) {
                    existingNextCategoryGamma.setName(nextCategoryGamma.getName());
                }
                if (nextCategoryGamma.getDescription() != null) {
                    existingNextCategoryGamma.setDescription(nextCategoryGamma.getDescription());
                }

                return existingNextCategoryGamma;
            })
            .map(nextCategoryGammaRepository::save);
    }

    /**
     * Get one nextCategoryGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryGamma> findOne(Long id) {
        LOG.debug("Request to get NextCategoryGamma : {}", id);
        return nextCategoryGammaRepository.findById(id);
    }

    /**
     * Delete the nextCategoryGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryGamma : {}", id);
        nextCategoryGammaRepository.deleteById(id);
    }
}
