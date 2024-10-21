package ai.realworld.service;

import ai.realworld.domain.OlAlmantinoMilo;
import ai.realworld.repository.OlAlmantinoMiloRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.OlAlmantinoMilo}.
 */
@Service
@Transactional
public class OlAlmantinoMiloService {

    private static final Logger LOG = LoggerFactory.getLogger(OlAlmantinoMiloService.class);

    private final OlAlmantinoMiloRepository olAlmantinoMiloRepository;

    public OlAlmantinoMiloService(OlAlmantinoMiloRepository olAlmantinoMiloRepository) {
        this.olAlmantinoMiloRepository = olAlmantinoMiloRepository;
    }

    /**
     * Save a olAlmantinoMilo.
     *
     * @param olAlmantinoMilo the entity to save.
     * @return the persisted entity.
     */
    public OlAlmantinoMilo save(OlAlmantinoMilo olAlmantinoMilo) {
        LOG.debug("Request to save OlAlmantinoMilo : {}", olAlmantinoMilo);
        return olAlmantinoMiloRepository.save(olAlmantinoMilo);
    }

    /**
     * Update a olAlmantinoMilo.
     *
     * @param olAlmantinoMilo the entity to save.
     * @return the persisted entity.
     */
    public OlAlmantinoMilo update(OlAlmantinoMilo olAlmantinoMilo) {
        LOG.debug("Request to update OlAlmantinoMilo : {}", olAlmantinoMilo);
        return olAlmantinoMiloRepository.save(olAlmantinoMilo);
    }

    /**
     * Partially update a olAlmantinoMilo.
     *
     * @param olAlmantinoMilo the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OlAlmantinoMilo> partialUpdate(OlAlmantinoMilo olAlmantinoMilo) {
        LOG.debug("Request to partially update OlAlmantinoMilo : {}", olAlmantinoMilo);

        return olAlmantinoMiloRepository
            .findById(olAlmantinoMilo.getId())
            .map(existingOlAlmantinoMilo -> {
                if (olAlmantinoMilo.getProvider() != null) {
                    existingOlAlmantinoMilo.setProvider(olAlmantinoMilo.getProvider());
                }
                if (olAlmantinoMilo.getProviderAppManagerId() != null) {
                    existingOlAlmantinoMilo.setProviderAppManagerId(olAlmantinoMilo.getProviderAppManagerId());
                }
                if (olAlmantinoMilo.getName() != null) {
                    existingOlAlmantinoMilo.setName(olAlmantinoMilo.getName());
                }
                if (olAlmantinoMilo.getProviderSecretKey() != null) {
                    existingOlAlmantinoMilo.setProviderSecretKey(olAlmantinoMilo.getProviderSecretKey());
                }
                if (olAlmantinoMilo.getProviderToken() != null) {
                    existingOlAlmantinoMilo.setProviderToken(olAlmantinoMilo.getProviderToken());
                }
                if (olAlmantinoMilo.getProviderRefreshToken() != null) {
                    existingOlAlmantinoMilo.setProviderRefreshToken(olAlmantinoMilo.getProviderRefreshToken());
                }

                return existingOlAlmantinoMilo;
            })
            .map(olAlmantinoMiloRepository::save);
    }

    /**
     * Get one olAlmantinoMilo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OlAlmantinoMilo> findOne(UUID id) {
        LOG.debug("Request to get OlAlmantinoMilo : {}", id);
        return olAlmantinoMiloRepository.findById(id);
    }

    /**
     * Delete the olAlmantinoMilo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete OlAlmantinoMilo : {}", id);
        olAlmantinoMiloRepository.deleteById(id);
    }
}
