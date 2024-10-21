package ai.realworld.service;

import ai.realworld.domain.AlLeandro;
import ai.realworld.repository.AlLeandroRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlLeandro}.
 */
@Service
@Transactional
public class AlLeandroService {

    private static final Logger LOG = LoggerFactory.getLogger(AlLeandroService.class);

    private final AlLeandroRepository alLeandroRepository;

    public AlLeandroService(AlLeandroRepository alLeandroRepository) {
        this.alLeandroRepository = alLeandroRepository;
    }

    /**
     * Save a alLeandro.
     *
     * @param alLeandro the entity to save.
     * @return the persisted entity.
     */
    public AlLeandro save(AlLeandro alLeandro) {
        LOG.debug("Request to save AlLeandro : {}", alLeandro);
        return alLeandroRepository.save(alLeandro);
    }

    /**
     * Update a alLeandro.
     *
     * @param alLeandro the entity to save.
     * @return the persisted entity.
     */
    public AlLeandro update(AlLeandro alLeandro) {
        LOG.debug("Request to update AlLeandro : {}", alLeandro);
        return alLeandroRepository.save(alLeandro);
    }

    /**
     * Partially update a alLeandro.
     *
     * @param alLeandro the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlLeandro> partialUpdate(AlLeandro alLeandro) {
        LOG.debug("Request to partially update AlLeandro : {}", alLeandro);

        return alLeandroRepository
            .findById(alLeandro.getId())
            .map(existingAlLeandro -> {
                if (alLeandro.getName() != null) {
                    existingAlLeandro.setName(alLeandro.getName());
                }
                if (alLeandro.getWeight() != null) {
                    existingAlLeandro.setWeight(alLeandro.getWeight());
                }
                if (alLeandro.getDescription() != null) {
                    existingAlLeandro.setDescription(alLeandro.getDescription());
                }
                if (alLeandro.getFromDate() != null) {
                    existingAlLeandro.setFromDate(alLeandro.getFromDate());
                }
                if (alLeandro.getToDate() != null) {
                    existingAlLeandro.setToDate(alLeandro.getToDate());
                }
                if (alLeandro.getIsEnabled() != null) {
                    existingAlLeandro.setIsEnabled(alLeandro.getIsEnabled());
                }
                if (alLeandro.getSeparateWinningByPeriods() != null) {
                    existingAlLeandro.setSeparateWinningByPeriods(alLeandro.getSeparateWinningByPeriods());
                }

                return existingAlLeandro;
            })
            .map(alLeandroRepository::save);
    }

    /**
     * Get one alLeandro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlLeandro> findOne(UUID id) {
        LOG.debug("Request to get AlLeandro : {}", id);
        return alLeandroRepository.findById(id);
    }

    /**
     * Delete the alLeandro by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlLeandro : {}", id);
        alLeandroRepository.deleteById(id);
    }
}
