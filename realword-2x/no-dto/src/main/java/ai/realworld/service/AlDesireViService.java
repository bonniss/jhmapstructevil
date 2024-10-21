package ai.realworld.service;

import ai.realworld.domain.AlDesireVi;
import ai.realworld.repository.AlDesireViRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlDesireVi}.
 */
@Service
@Transactional
public class AlDesireViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlDesireViService.class);

    private final AlDesireViRepository alDesireViRepository;

    public AlDesireViService(AlDesireViRepository alDesireViRepository) {
        this.alDesireViRepository = alDesireViRepository;
    }

    /**
     * Save a alDesireVi.
     *
     * @param alDesireVi the entity to save.
     * @return the persisted entity.
     */
    public AlDesireVi save(AlDesireVi alDesireVi) {
        LOG.debug("Request to save AlDesireVi : {}", alDesireVi);
        return alDesireViRepository.save(alDesireVi);
    }

    /**
     * Update a alDesireVi.
     *
     * @param alDesireVi the entity to save.
     * @return the persisted entity.
     */
    public AlDesireVi update(AlDesireVi alDesireVi) {
        LOG.debug("Request to update AlDesireVi : {}", alDesireVi);
        return alDesireViRepository.save(alDesireVi);
    }

    /**
     * Partially update a alDesireVi.
     *
     * @param alDesireVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlDesireVi> partialUpdate(AlDesireVi alDesireVi) {
        LOG.debug("Request to partially update AlDesireVi : {}", alDesireVi);

        return alDesireViRepository
            .findById(alDesireVi.getId())
            .map(existingAlDesireVi -> {
                if (alDesireVi.getName() != null) {
                    existingAlDesireVi.setName(alDesireVi.getName());
                }
                if (alDesireVi.getWeight() != null) {
                    existingAlDesireVi.setWeight(alDesireVi.getWeight());
                }
                if (alDesireVi.getProbabilityOfWinning() != null) {
                    existingAlDesireVi.setProbabilityOfWinning(alDesireVi.getProbabilityOfWinning());
                }
                if (alDesireVi.getMaximumWinningTime() != null) {
                    existingAlDesireVi.setMaximumWinningTime(alDesireVi.getMaximumWinningTime());
                }
                if (alDesireVi.getIsWinningTimeLimited() != null) {
                    existingAlDesireVi.setIsWinningTimeLimited(alDesireVi.getIsWinningTimeLimited());
                }
                if (alDesireVi.getAwardResultType() != null) {
                    existingAlDesireVi.setAwardResultType(alDesireVi.getAwardResultType());
                }
                if (alDesireVi.getAwardReference() != null) {
                    existingAlDesireVi.setAwardReference(alDesireVi.getAwardReference());
                }
                if (alDesireVi.getIsDefault() != null) {
                    existingAlDesireVi.setIsDefault(alDesireVi.getIsDefault());
                }

                return existingAlDesireVi;
            })
            .map(alDesireViRepository::save);
    }

    /**
     * Get one alDesireVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlDesireVi> findOne(UUID id) {
        LOG.debug("Request to get AlDesireVi : {}", id);
        return alDesireViRepository.findById(id);
    }

    /**
     * Delete the alDesireVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlDesireVi : {}", id);
        alDesireViRepository.deleteById(id);
    }
}
