package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryViVi;
import xyz.jhmapstruct.repository.NextCategoryViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryViVi}.
 */
@Service
@Transactional
public class NextCategoryViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryViViService.class);

    private final NextCategoryViViRepository nextCategoryViViRepository;

    public NextCategoryViViService(NextCategoryViViRepository nextCategoryViViRepository) {
        this.nextCategoryViViRepository = nextCategoryViViRepository;
    }

    /**
     * Save a nextCategoryViVi.
     *
     * @param nextCategoryViVi the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryViVi save(NextCategoryViVi nextCategoryViVi) {
        LOG.debug("Request to save NextCategoryViVi : {}", nextCategoryViVi);
        return nextCategoryViViRepository.save(nextCategoryViVi);
    }

    /**
     * Update a nextCategoryViVi.
     *
     * @param nextCategoryViVi the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryViVi update(NextCategoryViVi nextCategoryViVi) {
        LOG.debug("Request to update NextCategoryViVi : {}", nextCategoryViVi);
        return nextCategoryViViRepository.save(nextCategoryViVi);
    }

    /**
     * Partially update a nextCategoryViVi.
     *
     * @param nextCategoryViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryViVi> partialUpdate(NextCategoryViVi nextCategoryViVi) {
        LOG.debug("Request to partially update NextCategoryViVi : {}", nextCategoryViVi);

        return nextCategoryViViRepository
            .findById(nextCategoryViVi.getId())
            .map(existingNextCategoryViVi -> {
                if (nextCategoryViVi.getName() != null) {
                    existingNextCategoryViVi.setName(nextCategoryViVi.getName());
                }
                if (nextCategoryViVi.getDescription() != null) {
                    existingNextCategoryViVi.setDescription(nextCategoryViVi.getDescription());
                }

                return existingNextCategoryViVi;
            })
            .map(nextCategoryViViRepository::save);
    }

    /**
     * Get one nextCategoryViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryViVi> findOne(Long id) {
        LOG.debug("Request to get NextCategoryViVi : {}", id);
        return nextCategoryViViRepository.findById(id);
    }

    /**
     * Delete the nextCategoryViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryViVi : {}", id);
        nextCategoryViViRepository.deleteById(id);
    }
}
