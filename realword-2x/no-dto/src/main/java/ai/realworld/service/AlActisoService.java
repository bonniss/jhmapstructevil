package ai.realworld.service;

import ai.realworld.domain.AlActiso;
import ai.realworld.repository.AlActisoRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlActiso}.
 */
@Service
@Transactional
public class AlActisoService {

    private static final Logger LOG = LoggerFactory.getLogger(AlActisoService.class);

    private final AlActisoRepository alActisoRepository;

    public AlActisoService(AlActisoRepository alActisoRepository) {
        this.alActisoRepository = alActisoRepository;
    }

    /**
     * Save a alActiso.
     *
     * @param alActiso the entity to save.
     * @return the persisted entity.
     */
    public AlActiso save(AlActiso alActiso) {
        LOG.debug("Request to save AlActiso : {}", alActiso);
        return alActisoRepository.save(alActiso);
    }

    /**
     * Update a alActiso.
     *
     * @param alActiso the entity to save.
     * @return the persisted entity.
     */
    public AlActiso update(AlActiso alActiso) {
        LOG.debug("Request to update AlActiso : {}", alActiso);
        return alActisoRepository.save(alActiso);
    }

    /**
     * Partially update a alActiso.
     *
     * @param alActiso the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlActiso> partialUpdate(AlActiso alActiso) {
        LOG.debug("Request to partially update AlActiso : {}", alActiso);

        return alActisoRepository
            .findById(alActiso.getId())
            .map(existingAlActiso -> {
                if (alActiso.getKey() != null) {
                    existingAlActiso.setKey(alActiso.getKey());
                }
                if (alActiso.getValueJason() != null) {
                    existingAlActiso.setValueJason(alActiso.getValueJason());
                }

                return existingAlActiso;
            })
            .map(alActisoRepository::save);
    }

    /**
     * Get one alActiso by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlActiso> findOne(Long id) {
        LOG.debug("Request to get AlActiso : {}", id);
        return alActisoRepository.findById(id);
    }

    /**
     * Delete the alActiso by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlActiso : {}", id);
        alActisoRepository.deleteById(id);
    }
}
