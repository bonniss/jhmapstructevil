package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryVi;
import xyz.jhmapstruct.repository.NextCategoryViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryVi}.
 */
@Service
@Transactional
public class NextCategoryViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryViService.class);

    private final NextCategoryViRepository nextCategoryViRepository;

    public NextCategoryViService(NextCategoryViRepository nextCategoryViRepository) {
        this.nextCategoryViRepository = nextCategoryViRepository;
    }

    /**
     * Save a nextCategoryVi.
     *
     * @param nextCategoryVi the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryVi save(NextCategoryVi nextCategoryVi) {
        LOG.debug("Request to save NextCategoryVi : {}", nextCategoryVi);
        return nextCategoryViRepository.save(nextCategoryVi);
    }

    /**
     * Update a nextCategoryVi.
     *
     * @param nextCategoryVi the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryVi update(NextCategoryVi nextCategoryVi) {
        LOG.debug("Request to update NextCategoryVi : {}", nextCategoryVi);
        return nextCategoryViRepository.save(nextCategoryVi);
    }

    /**
     * Partially update a nextCategoryVi.
     *
     * @param nextCategoryVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryVi> partialUpdate(NextCategoryVi nextCategoryVi) {
        LOG.debug("Request to partially update NextCategoryVi : {}", nextCategoryVi);

        return nextCategoryViRepository
            .findById(nextCategoryVi.getId())
            .map(existingNextCategoryVi -> {
                if (nextCategoryVi.getName() != null) {
                    existingNextCategoryVi.setName(nextCategoryVi.getName());
                }
                if (nextCategoryVi.getDescription() != null) {
                    existingNextCategoryVi.setDescription(nextCategoryVi.getDescription());
                }

                return existingNextCategoryVi;
            })
            .map(nextCategoryViRepository::save);
    }

    /**
     * Get one nextCategoryVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryVi> findOne(Long id) {
        LOG.debug("Request to get NextCategoryVi : {}", id);
        return nextCategoryViRepository.findById(id);
    }

    /**
     * Delete the nextCategoryVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryVi : {}", id);
        nextCategoryViRepository.deleteById(id);
    }
}
