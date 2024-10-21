package ai.realworld.service;

import ai.realworld.domain.AlLeandroPlayingTimeVi;
import ai.realworld.repository.AlLeandroPlayingTimeViRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlLeandroPlayingTimeVi}.
 */
@Service
@Transactional
public class AlLeandroPlayingTimeViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlLeandroPlayingTimeViService.class);

    private final AlLeandroPlayingTimeViRepository alLeandroPlayingTimeViRepository;

    public AlLeandroPlayingTimeViService(AlLeandroPlayingTimeViRepository alLeandroPlayingTimeViRepository) {
        this.alLeandroPlayingTimeViRepository = alLeandroPlayingTimeViRepository;
    }

    /**
     * Save a alLeandroPlayingTimeVi.
     *
     * @param alLeandroPlayingTimeVi the entity to save.
     * @return the persisted entity.
     */
    public AlLeandroPlayingTimeVi save(AlLeandroPlayingTimeVi alLeandroPlayingTimeVi) {
        LOG.debug("Request to save AlLeandroPlayingTimeVi : {}", alLeandroPlayingTimeVi);
        return alLeandroPlayingTimeViRepository.save(alLeandroPlayingTimeVi);
    }

    /**
     * Update a alLeandroPlayingTimeVi.
     *
     * @param alLeandroPlayingTimeVi the entity to save.
     * @return the persisted entity.
     */
    public AlLeandroPlayingTimeVi update(AlLeandroPlayingTimeVi alLeandroPlayingTimeVi) {
        LOG.debug("Request to update AlLeandroPlayingTimeVi : {}", alLeandroPlayingTimeVi);
        return alLeandroPlayingTimeViRepository.save(alLeandroPlayingTimeVi);
    }

    /**
     * Partially update a alLeandroPlayingTimeVi.
     *
     * @param alLeandroPlayingTimeVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlLeandroPlayingTimeVi> partialUpdate(AlLeandroPlayingTimeVi alLeandroPlayingTimeVi) {
        LOG.debug("Request to partially update AlLeandroPlayingTimeVi : {}", alLeandroPlayingTimeVi);

        return alLeandroPlayingTimeViRepository
            .findById(alLeandroPlayingTimeVi.getId())
            .map(existingAlLeandroPlayingTimeVi -> {
                if (alLeandroPlayingTimeVi.getStatus() != null) {
                    existingAlLeandroPlayingTimeVi.setStatus(alLeandroPlayingTimeVi.getStatus());
                }
                if (alLeandroPlayingTimeVi.getWonDate() != null) {
                    existingAlLeandroPlayingTimeVi.setWonDate(alLeandroPlayingTimeVi.getWonDate());
                }
                if (alLeandroPlayingTimeVi.getSentAwardToPlayerAt() != null) {
                    existingAlLeandroPlayingTimeVi.setSentAwardToPlayerAt(alLeandroPlayingTimeVi.getSentAwardToPlayerAt());
                }
                if (alLeandroPlayingTimeVi.getSentAwardToPlayerBy() != null) {
                    existingAlLeandroPlayingTimeVi.setSentAwardToPlayerBy(alLeandroPlayingTimeVi.getSentAwardToPlayerBy());
                }
                if (alLeandroPlayingTimeVi.getPlayerReceivedTheAwardAt() != null) {
                    existingAlLeandroPlayingTimeVi.setPlayerReceivedTheAwardAt(alLeandroPlayingTimeVi.getPlayerReceivedTheAwardAt());
                }
                if (alLeandroPlayingTimeVi.getPlaySourceTime() != null) {
                    existingAlLeandroPlayingTimeVi.setPlaySourceTime(alLeandroPlayingTimeVi.getPlaySourceTime());
                }

                return existingAlLeandroPlayingTimeVi;
            })
            .map(alLeandroPlayingTimeViRepository::save);
    }

    /**
     * Get one alLeandroPlayingTimeVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlLeandroPlayingTimeVi> findOne(UUID id) {
        LOG.debug("Request to get AlLeandroPlayingTimeVi : {}", id);
        return alLeandroPlayingTimeViRepository.findById(id);
    }

    /**
     * Delete the alLeandroPlayingTimeVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlLeandroPlayingTimeVi : {}", id);
        alLeandroPlayingTimeViRepository.deleteById(id);
    }
}
