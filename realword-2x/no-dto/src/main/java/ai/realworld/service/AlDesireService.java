package ai.realworld.service;

import ai.realworld.domain.AlDesire;
import ai.realworld.repository.AlDesireRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlDesire}.
 */
@Service
@Transactional
public class AlDesireService {

    private static final Logger LOG = LoggerFactory.getLogger(AlDesireService.class);

    private final AlDesireRepository alDesireRepository;

    public AlDesireService(AlDesireRepository alDesireRepository) {
        this.alDesireRepository = alDesireRepository;
    }

    /**
     * Save a alDesire.
     *
     * @param alDesire the entity to save.
     * @return the persisted entity.
     */
    public AlDesire save(AlDesire alDesire) {
        LOG.debug("Request to save AlDesire : {}", alDesire);
        return alDesireRepository.save(alDesire);
    }

    /**
     * Update a alDesire.
     *
     * @param alDesire the entity to save.
     * @return the persisted entity.
     */
    public AlDesire update(AlDesire alDesire) {
        LOG.debug("Request to update AlDesire : {}", alDesire);
        return alDesireRepository.save(alDesire);
    }

    /**
     * Partially update a alDesire.
     *
     * @param alDesire the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlDesire> partialUpdate(AlDesire alDesire) {
        LOG.debug("Request to partially update AlDesire : {}", alDesire);

        return alDesireRepository
            .findById(alDesire.getId())
            .map(existingAlDesire -> {
                if (alDesire.getName() != null) {
                    existingAlDesire.setName(alDesire.getName());
                }
                if (alDesire.getWeight() != null) {
                    existingAlDesire.setWeight(alDesire.getWeight());
                }
                if (alDesire.getProbabilityOfWinning() != null) {
                    existingAlDesire.setProbabilityOfWinning(alDesire.getProbabilityOfWinning());
                }
                if (alDesire.getMaximumWinningTime() != null) {
                    existingAlDesire.setMaximumWinningTime(alDesire.getMaximumWinningTime());
                }
                if (alDesire.getIsWinningTimeLimited() != null) {
                    existingAlDesire.setIsWinningTimeLimited(alDesire.getIsWinningTimeLimited());
                }
                if (alDesire.getAwardResultType() != null) {
                    existingAlDesire.setAwardResultType(alDesire.getAwardResultType());
                }
                if (alDesire.getAwardReference() != null) {
                    existingAlDesire.setAwardReference(alDesire.getAwardReference());
                }
                if (alDesire.getIsDefault() != null) {
                    existingAlDesire.setIsDefault(alDesire.getIsDefault());
                }

                return existingAlDesire;
            })
            .map(alDesireRepository::save);
    }

    /**
     * Get one alDesire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlDesire> findOne(UUID id) {
        LOG.debug("Request to get AlDesire : {}", id);
        return alDesireRepository.findById(id);
    }

    /**
     * Delete the alDesire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlDesire : {}", id);
        alDesireRepository.deleteById(id);
    }
}
