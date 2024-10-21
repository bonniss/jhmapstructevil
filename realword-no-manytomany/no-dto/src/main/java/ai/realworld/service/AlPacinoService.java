package ai.realworld.service;

import ai.realworld.domain.AlPacino;
import ai.realworld.repository.AlPacinoRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPacino}.
 */
@Service
@Transactional
public class AlPacinoService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoService.class);

    private final AlPacinoRepository alPacinoRepository;

    public AlPacinoService(AlPacinoRepository alPacinoRepository) {
        this.alPacinoRepository = alPacinoRepository;
    }

    /**
     * Save a alPacino.
     *
     * @param alPacino the entity to save.
     * @return the persisted entity.
     */
    public AlPacino save(AlPacino alPacino) {
        LOG.debug("Request to save AlPacino : {}", alPacino);
        return alPacinoRepository.save(alPacino);
    }

    /**
     * Update a alPacino.
     *
     * @param alPacino the entity to save.
     * @return the persisted entity.
     */
    public AlPacino update(AlPacino alPacino) {
        LOG.debug("Request to update AlPacino : {}", alPacino);
        return alPacinoRepository.save(alPacino);
    }

    /**
     * Partially update a alPacino.
     *
     * @param alPacino the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPacino> partialUpdate(AlPacino alPacino) {
        LOG.debug("Request to partially update AlPacino : {}", alPacino);

        return alPacinoRepository
            .findById(alPacino.getId())
            .map(existingAlPacino -> {
                if (alPacino.getPlatformCode() != null) {
                    existingAlPacino.setPlatformCode(alPacino.getPlatformCode());
                }
                if (alPacino.getPlatformUsername() != null) {
                    existingAlPacino.setPlatformUsername(alPacino.getPlatformUsername());
                }
                if (alPacino.getPlatformAvatarUrl() != null) {
                    existingAlPacino.setPlatformAvatarUrl(alPacino.getPlatformAvatarUrl());
                }
                if (alPacino.getIsSensitive() != null) {
                    existingAlPacino.setIsSensitive(alPacino.getIsSensitive());
                }
                if (alPacino.getFamilyName() != null) {
                    existingAlPacino.setFamilyName(alPacino.getFamilyName());
                }
                if (alPacino.getGivenName() != null) {
                    existingAlPacino.setGivenName(alPacino.getGivenName());
                }
                if (alPacino.getDob() != null) {
                    existingAlPacino.setDob(alPacino.getDob());
                }
                if (alPacino.getGender() != null) {
                    existingAlPacino.setGender(alPacino.getGender());
                }
                if (alPacino.getPhone() != null) {
                    existingAlPacino.setPhone(alPacino.getPhone());
                }
                if (alPacino.getEmail() != null) {
                    existingAlPacino.setEmail(alPacino.getEmail());
                }
                if (alPacino.getAcquiredFrom() != null) {
                    existingAlPacino.setAcquiredFrom(alPacino.getAcquiredFrom());
                }
                if (alPacino.getCurrentPoints() != null) {
                    existingAlPacino.setCurrentPoints(alPacino.getCurrentPoints());
                }
                if (alPacino.getTotalPoints() != null) {
                    existingAlPacino.setTotalPoints(alPacino.getTotalPoints());
                }
                if (alPacino.getIsFollowing() != null) {
                    existingAlPacino.setIsFollowing(alPacino.getIsFollowing());
                }
                if (alPacino.getIsEnabled() != null) {
                    existingAlPacino.setIsEnabled(alPacino.getIsEnabled());
                }

                return existingAlPacino;
            })
            .map(alPacinoRepository::save);
    }

    /**
     * Get one alPacino by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPacino> findOne(UUID id) {
        LOG.debug("Request to get AlPacino : {}", id);
        return alPacinoRepository.findById(id);
    }

    /**
     * Delete the alPacino by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlPacino : {}", id);
        alPacinoRepository.deleteById(id);
    }
}
