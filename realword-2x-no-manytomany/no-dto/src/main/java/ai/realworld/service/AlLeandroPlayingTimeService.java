package ai.realworld.service;

import ai.realworld.domain.AlLeandroPlayingTime;
import ai.realworld.repository.AlLeandroPlayingTimeRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlLeandroPlayingTime}.
 */
@Service
@Transactional
public class AlLeandroPlayingTimeService {

    private static final Logger LOG = LoggerFactory.getLogger(AlLeandroPlayingTimeService.class);

    private final AlLeandroPlayingTimeRepository alLeandroPlayingTimeRepository;

    public AlLeandroPlayingTimeService(AlLeandroPlayingTimeRepository alLeandroPlayingTimeRepository) {
        this.alLeandroPlayingTimeRepository = alLeandroPlayingTimeRepository;
    }

    /**
     * Save a alLeandroPlayingTime.
     *
     * @param alLeandroPlayingTime the entity to save.
     * @return the persisted entity.
     */
    public AlLeandroPlayingTime save(AlLeandroPlayingTime alLeandroPlayingTime) {
        LOG.debug("Request to save AlLeandroPlayingTime : {}", alLeandroPlayingTime);
        return alLeandroPlayingTimeRepository.save(alLeandroPlayingTime);
    }

    /**
     * Update a alLeandroPlayingTime.
     *
     * @param alLeandroPlayingTime the entity to save.
     * @return the persisted entity.
     */
    public AlLeandroPlayingTime update(AlLeandroPlayingTime alLeandroPlayingTime) {
        LOG.debug("Request to update AlLeandroPlayingTime : {}", alLeandroPlayingTime);
        return alLeandroPlayingTimeRepository.save(alLeandroPlayingTime);
    }

    /**
     * Partially update a alLeandroPlayingTime.
     *
     * @param alLeandroPlayingTime the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlLeandroPlayingTime> partialUpdate(AlLeandroPlayingTime alLeandroPlayingTime) {
        LOG.debug("Request to partially update AlLeandroPlayingTime : {}", alLeandroPlayingTime);

        return alLeandroPlayingTimeRepository
            .findById(alLeandroPlayingTime.getId())
            .map(existingAlLeandroPlayingTime -> {
                if (alLeandroPlayingTime.getStatus() != null) {
                    existingAlLeandroPlayingTime.setStatus(alLeandroPlayingTime.getStatus());
                }
                if (alLeandroPlayingTime.getWonDate() != null) {
                    existingAlLeandroPlayingTime.setWonDate(alLeandroPlayingTime.getWonDate());
                }
                if (alLeandroPlayingTime.getSentAwardToPlayerAt() != null) {
                    existingAlLeandroPlayingTime.setSentAwardToPlayerAt(alLeandroPlayingTime.getSentAwardToPlayerAt());
                }
                if (alLeandroPlayingTime.getSentAwardToPlayerBy() != null) {
                    existingAlLeandroPlayingTime.setSentAwardToPlayerBy(alLeandroPlayingTime.getSentAwardToPlayerBy());
                }
                if (alLeandroPlayingTime.getPlayerReceivedTheAwardAt() != null) {
                    existingAlLeandroPlayingTime.setPlayerReceivedTheAwardAt(alLeandroPlayingTime.getPlayerReceivedTheAwardAt());
                }
                if (alLeandroPlayingTime.getPlaySourceTime() != null) {
                    existingAlLeandroPlayingTime.setPlaySourceTime(alLeandroPlayingTime.getPlaySourceTime());
                }

                return existingAlLeandroPlayingTime;
            })
            .map(alLeandroPlayingTimeRepository::save);
    }

    /**
     * Get one alLeandroPlayingTime by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlLeandroPlayingTime> findOne(UUID id) {
        LOG.debug("Request to get AlLeandroPlayingTime : {}", id);
        return alLeandroPlayingTimeRepository.findById(id);
    }

    /**
     * Delete the alLeandroPlayingTime by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlLeandroPlayingTime : {}", id);
        alLeandroPlayingTimeRepository.deleteById(id);
    }
}
