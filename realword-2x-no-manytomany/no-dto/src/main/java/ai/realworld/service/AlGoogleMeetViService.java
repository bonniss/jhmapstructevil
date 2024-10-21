package ai.realworld.service;

import ai.realworld.domain.AlGoogleMeetVi;
import ai.realworld.repository.AlGoogleMeetViRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlGoogleMeetVi}.
 */
@Service
@Transactional
public class AlGoogleMeetViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoogleMeetViService.class);

    private final AlGoogleMeetViRepository alGoogleMeetViRepository;

    public AlGoogleMeetViService(AlGoogleMeetViRepository alGoogleMeetViRepository) {
        this.alGoogleMeetViRepository = alGoogleMeetViRepository;
    }

    /**
     * Save a alGoogleMeetVi.
     *
     * @param alGoogleMeetVi the entity to save.
     * @return the persisted entity.
     */
    public AlGoogleMeetVi save(AlGoogleMeetVi alGoogleMeetVi) {
        LOG.debug("Request to save AlGoogleMeetVi : {}", alGoogleMeetVi);
        return alGoogleMeetViRepository.save(alGoogleMeetVi);
    }

    /**
     * Update a alGoogleMeetVi.
     *
     * @param alGoogleMeetVi the entity to save.
     * @return the persisted entity.
     */
    public AlGoogleMeetVi update(AlGoogleMeetVi alGoogleMeetVi) {
        LOG.debug("Request to update AlGoogleMeetVi : {}", alGoogleMeetVi);
        return alGoogleMeetViRepository.save(alGoogleMeetVi);
    }

    /**
     * Partially update a alGoogleMeetVi.
     *
     * @param alGoogleMeetVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlGoogleMeetVi> partialUpdate(AlGoogleMeetVi alGoogleMeetVi) {
        LOG.debug("Request to partially update AlGoogleMeetVi : {}", alGoogleMeetVi);

        return alGoogleMeetViRepository
            .findById(alGoogleMeetVi.getId())
            .map(existingAlGoogleMeetVi -> {
                if (alGoogleMeetVi.getTitle() != null) {
                    existingAlGoogleMeetVi.setTitle(alGoogleMeetVi.getTitle());
                }
                if (alGoogleMeetVi.getDescription() != null) {
                    existingAlGoogleMeetVi.setDescription(alGoogleMeetVi.getDescription());
                }
                if (alGoogleMeetVi.getNumberOfParticipants() != null) {
                    existingAlGoogleMeetVi.setNumberOfParticipants(alGoogleMeetVi.getNumberOfParticipants());
                }
                if (alGoogleMeetVi.getPlannedDate() != null) {
                    existingAlGoogleMeetVi.setPlannedDate(alGoogleMeetVi.getPlannedDate());
                }
                if (alGoogleMeetVi.getPlannedDurationInMinutes() != null) {
                    existingAlGoogleMeetVi.setPlannedDurationInMinutes(alGoogleMeetVi.getPlannedDurationInMinutes());
                }
                if (alGoogleMeetVi.getActualDate() != null) {
                    existingAlGoogleMeetVi.setActualDate(alGoogleMeetVi.getActualDate());
                }
                if (alGoogleMeetVi.getActualDurationInMinutes() != null) {
                    existingAlGoogleMeetVi.setActualDurationInMinutes(alGoogleMeetVi.getActualDurationInMinutes());
                }
                if (alGoogleMeetVi.getContentJason() != null) {
                    existingAlGoogleMeetVi.setContentJason(alGoogleMeetVi.getContentJason());
                }

                return existingAlGoogleMeetVi;
            })
            .map(alGoogleMeetViRepository::save);
    }

    /**
     * Get one alGoogleMeetVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlGoogleMeetVi> findOne(UUID id) {
        LOG.debug("Request to get AlGoogleMeetVi : {}", id);
        return alGoogleMeetViRepository.findById(id);
    }

    /**
     * Delete the alGoogleMeetVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlGoogleMeetVi : {}", id);
        alGoogleMeetViRepository.deleteById(id);
    }
}
