package ai.realworld.service;

import ai.realworld.domain.AlGoogleMeet;
import ai.realworld.repository.AlGoogleMeetRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlGoogleMeet}.
 */
@Service
@Transactional
public class AlGoogleMeetService {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoogleMeetService.class);

    private final AlGoogleMeetRepository alGoogleMeetRepository;

    public AlGoogleMeetService(AlGoogleMeetRepository alGoogleMeetRepository) {
        this.alGoogleMeetRepository = alGoogleMeetRepository;
    }

    /**
     * Save a alGoogleMeet.
     *
     * @param alGoogleMeet the entity to save.
     * @return the persisted entity.
     */
    public AlGoogleMeet save(AlGoogleMeet alGoogleMeet) {
        LOG.debug("Request to save AlGoogleMeet : {}", alGoogleMeet);
        return alGoogleMeetRepository.save(alGoogleMeet);
    }

    /**
     * Update a alGoogleMeet.
     *
     * @param alGoogleMeet the entity to save.
     * @return the persisted entity.
     */
    public AlGoogleMeet update(AlGoogleMeet alGoogleMeet) {
        LOG.debug("Request to update AlGoogleMeet : {}", alGoogleMeet);
        return alGoogleMeetRepository.save(alGoogleMeet);
    }

    /**
     * Partially update a alGoogleMeet.
     *
     * @param alGoogleMeet the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlGoogleMeet> partialUpdate(AlGoogleMeet alGoogleMeet) {
        LOG.debug("Request to partially update AlGoogleMeet : {}", alGoogleMeet);

        return alGoogleMeetRepository
            .findById(alGoogleMeet.getId())
            .map(existingAlGoogleMeet -> {
                if (alGoogleMeet.getTitle() != null) {
                    existingAlGoogleMeet.setTitle(alGoogleMeet.getTitle());
                }
                if (alGoogleMeet.getDescription() != null) {
                    existingAlGoogleMeet.setDescription(alGoogleMeet.getDescription());
                }
                if (alGoogleMeet.getNumberOfParticipants() != null) {
                    existingAlGoogleMeet.setNumberOfParticipants(alGoogleMeet.getNumberOfParticipants());
                }
                if (alGoogleMeet.getPlannedDate() != null) {
                    existingAlGoogleMeet.setPlannedDate(alGoogleMeet.getPlannedDate());
                }
                if (alGoogleMeet.getPlannedDurationInMinutes() != null) {
                    existingAlGoogleMeet.setPlannedDurationInMinutes(alGoogleMeet.getPlannedDurationInMinutes());
                }
                if (alGoogleMeet.getActualDate() != null) {
                    existingAlGoogleMeet.setActualDate(alGoogleMeet.getActualDate());
                }
                if (alGoogleMeet.getActualDurationInMinutes() != null) {
                    existingAlGoogleMeet.setActualDurationInMinutes(alGoogleMeet.getActualDurationInMinutes());
                }
                if (alGoogleMeet.getContentJason() != null) {
                    existingAlGoogleMeet.setContentJason(alGoogleMeet.getContentJason());
                }

                return existingAlGoogleMeet;
            })
            .map(alGoogleMeetRepository::save);
    }

    /**
     * Get one alGoogleMeet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlGoogleMeet> findOne(UUID id) {
        LOG.debug("Request to get AlGoogleMeet : {}", id);
        return alGoogleMeetRepository.findById(id);
    }

    /**
     * Delete the alGoogleMeet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlGoogleMeet : {}", id);
        alGoogleMeetRepository.deleteById(id);
    }
}
