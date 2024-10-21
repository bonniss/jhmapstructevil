package ai.realworld.service;

import ai.realworld.domain.SaisanCogVi;
import ai.realworld.repository.SaisanCogViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.SaisanCogVi}.
 */
@Service
@Transactional
public class SaisanCogViService {

    private static final Logger LOG = LoggerFactory.getLogger(SaisanCogViService.class);

    private final SaisanCogViRepository saisanCogViRepository;

    public SaisanCogViService(SaisanCogViRepository saisanCogViRepository) {
        this.saisanCogViRepository = saisanCogViRepository;
    }

    /**
     * Save a saisanCogVi.
     *
     * @param saisanCogVi the entity to save.
     * @return the persisted entity.
     */
    public SaisanCogVi save(SaisanCogVi saisanCogVi) {
        LOG.debug("Request to save SaisanCogVi : {}", saisanCogVi);
        return saisanCogViRepository.save(saisanCogVi);
    }

    /**
     * Update a saisanCogVi.
     *
     * @param saisanCogVi the entity to save.
     * @return the persisted entity.
     */
    public SaisanCogVi update(SaisanCogVi saisanCogVi) {
        LOG.debug("Request to update SaisanCogVi : {}", saisanCogVi);
        return saisanCogViRepository.save(saisanCogVi);
    }

    /**
     * Partially update a saisanCogVi.
     *
     * @param saisanCogVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SaisanCogVi> partialUpdate(SaisanCogVi saisanCogVi) {
        LOG.debug("Request to partially update SaisanCogVi : {}", saisanCogVi);

        return saisanCogViRepository
            .findById(saisanCogVi.getId())
            .map(existingSaisanCogVi -> {
                if (saisanCogVi.getKey() != null) {
                    existingSaisanCogVi.setKey(saisanCogVi.getKey());
                }
                if (saisanCogVi.getValueJason() != null) {
                    existingSaisanCogVi.setValueJason(saisanCogVi.getValueJason());
                }

                return existingSaisanCogVi;
            })
            .map(saisanCogViRepository::save);
    }

    /**
     * Get one saisanCogVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SaisanCogVi> findOne(Long id) {
        LOG.debug("Request to get SaisanCogVi : {}", id);
        return saisanCogViRepository.findById(id);
    }

    /**
     * Delete the saisanCogVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SaisanCogVi : {}", id);
        saisanCogViRepository.deleteById(id);
    }
}
