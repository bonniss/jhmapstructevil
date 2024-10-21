package ai.realworld.service;

import ai.realworld.domain.SaisanCog;
import ai.realworld.repository.SaisanCogRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.SaisanCog}.
 */
@Service
@Transactional
public class SaisanCogService {

    private static final Logger LOG = LoggerFactory.getLogger(SaisanCogService.class);

    private final SaisanCogRepository saisanCogRepository;

    public SaisanCogService(SaisanCogRepository saisanCogRepository) {
        this.saisanCogRepository = saisanCogRepository;
    }

    /**
     * Save a saisanCog.
     *
     * @param saisanCog the entity to save.
     * @return the persisted entity.
     */
    public SaisanCog save(SaisanCog saisanCog) {
        LOG.debug("Request to save SaisanCog : {}", saisanCog);
        return saisanCogRepository.save(saisanCog);
    }

    /**
     * Update a saisanCog.
     *
     * @param saisanCog the entity to save.
     * @return the persisted entity.
     */
    public SaisanCog update(SaisanCog saisanCog) {
        LOG.debug("Request to update SaisanCog : {}", saisanCog);
        return saisanCogRepository.save(saisanCog);
    }

    /**
     * Partially update a saisanCog.
     *
     * @param saisanCog the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SaisanCog> partialUpdate(SaisanCog saisanCog) {
        LOG.debug("Request to partially update SaisanCog : {}", saisanCog);

        return saisanCogRepository
            .findById(saisanCog.getId())
            .map(existingSaisanCog -> {
                if (saisanCog.getKey() != null) {
                    existingSaisanCog.setKey(saisanCog.getKey());
                }
                if (saisanCog.getValueJason() != null) {
                    existingSaisanCog.setValueJason(saisanCog.getValueJason());
                }

                return existingSaisanCog;
            })
            .map(saisanCogRepository::save);
    }

    /**
     * Get one saisanCog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SaisanCog> findOne(Long id) {
        LOG.debug("Request to get SaisanCog : {}", id);
        return saisanCogRepository.findById(id);
    }

    /**
     * Delete the saisanCog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SaisanCog : {}", id);
        saisanCogRepository.deleteById(id);
    }
}
