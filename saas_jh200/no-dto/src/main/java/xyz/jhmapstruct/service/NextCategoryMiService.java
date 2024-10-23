package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryMi;
import xyz.jhmapstruct.repository.NextCategoryMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryMi}.
 */
@Service
@Transactional
public class NextCategoryMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryMiService.class);

    private final NextCategoryMiRepository nextCategoryMiRepository;

    public NextCategoryMiService(NextCategoryMiRepository nextCategoryMiRepository) {
        this.nextCategoryMiRepository = nextCategoryMiRepository;
    }

    /**
     * Save a nextCategoryMi.
     *
     * @param nextCategoryMi the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryMi save(NextCategoryMi nextCategoryMi) {
        LOG.debug("Request to save NextCategoryMi : {}", nextCategoryMi);
        return nextCategoryMiRepository.save(nextCategoryMi);
    }

    /**
     * Update a nextCategoryMi.
     *
     * @param nextCategoryMi the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryMi update(NextCategoryMi nextCategoryMi) {
        LOG.debug("Request to update NextCategoryMi : {}", nextCategoryMi);
        return nextCategoryMiRepository.save(nextCategoryMi);
    }

    /**
     * Partially update a nextCategoryMi.
     *
     * @param nextCategoryMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryMi> partialUpdate(NextCategoryMi nextCategoryMi) {
        LOG.debug("Request to partially update NextCategoryMi : {}", nextCategoryMi);

        return nextCategoryMiRepository
            .findById(nextCategoryMi.getId())
            .map(existingNextCategoryMi -> {
                if (nextCategoryMi.getName() != null) {
                    existingNextCategoryMi.setName(nextCategoryMi.getName());
                }
                if (nextCategoryMi.getDescription() != null) {
                    existingNextCategoryMi.setDescription(nextCategoryMi.getDescription());
                }

                return existingNextCategoryMi;
            })
            .map(nextCategoryMiRepository::save);
    }

    /**
     * Get one nextCategoryMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryMi> findOne(Long id) {
        LOG.debug("Request to get NextCategoryMi : {}", id);
        return nextCategoryMiRepository.findById(id);
    }

    /**
     * Delete the nextCategoryMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryMi : {}", id);
        nextCategoryMiRepository.deleteById(id);
    }
}
