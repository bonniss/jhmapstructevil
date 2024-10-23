package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategorySigma;
import xyz.jhmapstruct.repository.NextCategorySigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategorySigma}.
 */
@Service
@Transactional
public class NextCategorySigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategorySigmaService.class);

    private final NextCategorySigmaRepository nextCategorySigmaRepository;

    public NextCategorySigmaService(NextCategorySigmaRepository nextCategorySigmaRepository) {
        this.nextCategorySigmaRepository = nextCategorySigmaRepository;
    }

    /**
     * Save a nextCategorySigma.
     *
     * @param nextCategorySigma the entity to save.
     * @return the persisted entity.
     */
    public NextCategorySigma save(NextCategorySigma nextCategorySigma) {
        LOG.debug("Request to save NextCategorySigma : {}", nextCategorySigma);
        return nextCategorySigmaRepository.save(nextCategorySigma);
    }

    /**
     * Update a nextCategorySigma.
     *
     * @param nextCategorySigma the entity to save.
     * @return the persisted entity.
     */
    public NextCategorySigma update(NextCategorySigma nextCategorySigma) {
        LOG.debug("Request to update NextCategorySigma : {}", nextCategorySigma);
        return nextCategorySigmaRepository.save(nextCategorySigma);
    }

    /**
     * Partially update a nextCategorySigma.
     *
     * @param nextCategorySigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategorySigma> partialUpdate(NextCategorySigma nextCategorySigma) {
        LOG.debug("Request to partially update NextCategorySigma : {}", nextCategorySigma);

        return nextCategorySigmaRepository
            .findById(nextCategorySigma.getId())
            .map(existingNextCategorySigma -> {
                if (nextCategorySigma.getName() != null) {
                    existingNextCategorySigma.setName(nextCategorySigma.getName());
                }
                if (nextCategorySigma.getDescription() != null) {
                    existingNextCategorySigma.setDescription(nextCategorySigma.getDescription());
                }

                return existingNextCategorySigma;
            })
            .map(nextCategorySigmaRepository::save);
    }

    /**
     * Get one nextCategorySigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategorySigma> findOne(Long id) {
        LOG.debug("Request to get NextCategorySigma : {}", id);
        return nextCategorySigmaRepository.findById(id);
    }

    /**
     * Delete the nextCategorySigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategorySigma : {}", id);
        nextCategorySigmaRepository.deleteById(id);
    }
}
