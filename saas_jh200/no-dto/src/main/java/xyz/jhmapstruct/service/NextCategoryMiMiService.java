package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryMiMi;
import xyz.jhmapstruct.repository.NextCategoryMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryMiMi}.
 */
@Service
@Transactional
public class NextCategoryMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryMiMiService.class);

    private final NextCategoryMiMiRepository nextCategoryMiMiRepository;

    public NextCategoryMiMiService(NextCategoryMiMiRepository nextCategoryMiMiRepository) {
        this.nextCategoryMiMiRepository = nextCategoryMiMiRepository;
    }

    /**
     * Save a nextCategoryMiMi.
     *
     * @param nextCategoryMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryMiMi save(NextCategoryMiMi nextCategoryMiMi) {
        LOG.debug("Request to save NextCategoryMiMi : {}", nextCategoryMiMi);
        return nextCategoryMiMiRepository.save(nextCategoryMiMi);
    }

    /**
     * Update a nextCategoryMiMi.
     *
     * @param nextCategoryMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryMiMi update(NextCategoryMiMi nextCategoryMiMi) {
        LOG.debug("Request to update NextCategoryMiMi : {}", nextCategoryMiMi);
        return nextCategoryMiMiRepository.save(nextCategoryMiMi);
    }

    /**
     * Partially update a nextCategoryMiMi.
     *
     * @param nextCategoryMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryMiMi> partialUpdate(NextCategoryMiMi nextCategoryMiMi) {
        LOG.debug("Request to partially update NextCategoryMiMi : {}", nextCategoryMiMi);

        return nextCategoryMiMiRepository
            .findById(nextCategoryMiMi.getId())
            .map(existingNextCategoryMiMi -> {
                if (nextCategoryMiMi.getName() != null) {
                    existingNextCategoryMiMi.setName(nextCategoryMiMi.getName());
                }
                if (nextCategoryMiMi.getDescription() != null) {
                    existingNextCategoryMiMi.setDescription(nextCategoryMiMi.getDescription());
                }

                return existingNextCategoryMiMi;
            })
            .map(nextCategoryMiMiRepository::save);
    }

    /**
     * Get one nextCategoryMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryMiMi> findOne(Long id) {
        LOG.debug("Request to get NextCategoryMiMi : {}", id);
        return nextCategoryMiMiRepository.findById(id);
    }

    /**
     * Delete the nextCategoryMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryMiMi : {}", id);
        nextCategoryMiMiRepository.deleteById(id);
    }
}
