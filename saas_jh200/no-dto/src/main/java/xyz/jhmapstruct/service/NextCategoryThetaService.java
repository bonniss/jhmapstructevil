package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryTheta;
import xyz.jhmapstruct.repository.NextCategoryThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryTheta}.
 */
@Service
@Transactional
public class NextCategoryThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryThetaService.class);

    private final NextCategoryThetaRepository nextCategoryThetaRepository;

    public NextCategoryThetaService(NextCategoryThetaRepository nextCategoryThetaRepository) {
        this.nextCategoryThetaRepository = nextCategoryThetaRepository;
    }

    /**
     * Save a nextCategoryTheta.
     *
     * @param nextCategoryTheta the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryTheta save(NextCategoryTheta nextCategoryTheta) {
        LOG.debug("Request to save NextCategoryTheta : {}", nextCategoryTheta);
        return nextCategoryThetaRepository.save(nextCategoryTheta);
    }

    /**
     * Update a nextCategoryTheta.
     *
     * @param nextCategoryTheta the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryTheta update(NextCategoryTheta nextCategoryTheta) {
        LOG.debug("Request to update NextCategoryTheta : {}", nextCategoryTheta);
        return nextCategoryThetaRepository.save(nextCategoryTheta);
    }

    /**
     * Partially update a nextCategoryTheta.
     *
     * @param nextCategoryTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryTheta> partialUpdate(NextCategoryTheta nextCategoryTheta) {
        LOG.debug("Request to partially update NextCategoryTheta : {}", nextCategoryTheta);

        return nextCategoryThetaRepository
            .findById(nextCategoryTheta.getId())
            .map(existingNextCategoryTheta -> {
                if (nextCategoryTheta.getName() != null) {
                    existingNextCategoryTheta.setName(nextCategoryTheta.getName());
                }
                if (nextCategoryTheta.getDescription() != null) {
                    existingNextCategoryTheta.setDescription(nextCategoryTheta.getDescription());
                }

                return existingNextCategoryTheta;
            })
            .map(nextCategoryThetaRepository::save);
    }

    /**
     * Get one nextCategoryTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryTheta> findOne(Long id) {
        LOG.debug("Request to get NextCategoryTheta : {}", id);
        return nextCategoryThetaRepository.findById(id);
    }

    /**
     * Delete the nextCategoryTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryTheta : {}", id);
        nextCategoryThetaRepository.deleteById(id);
    }
}
