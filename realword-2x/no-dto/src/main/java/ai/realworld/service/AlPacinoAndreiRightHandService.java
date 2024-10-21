package ai.realworld.service;

import ai.realworld.domain.AlPacinoAndreiRightHand;
import ai.realworld.repository.AlPacinoAndreiRightHandRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPacinoAndreiRightHand}.
 */
@Service
@Transactional
public class AlPacinoAndreiRightHandService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoAndreiRightHandService.class);

    private final AlPacinoAndreiRightHandRepository alPacinoAndreiRightHandRepository;

    public AlPacinoAndreiRightHandService(AlPacinoAndreiRightHandRepository alPacinoAndreiRightHandRepository) {
        this.alPacinoAndreiRightHandRepository = alPacinoAndreiRightHandRepository;
    }

    /**
     * Save a alPacinoAndreiRightHand.
     *
     * @param alPacinoAndreiRightHand the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoAndreiRightHand save(AlPacinoAndreiRightHand alPacinoAndreiRightHand) {
        LOG.debug("Request to save AlPacinoAndreiRightHand : {}", alPacinoAndreiRightHand);
        return alPacinoAndreiRightHandRepository.save(alPacinoAndreiRightHand);
    }

    /**
     * Update a alPacinoAndreiRightHand.
     *
     * @param alPacinoAndreiRightHand the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoAndreiRightHand update(AlPacinoAndreiRightHand alPacinoAndreiRightHand) {
        LOG.debug("Request to update AlPacinoAndreiRightHand : {}", alPacinoAndreiRightHand);
        return alPacinoAndreiRightHandRepository.save(alPacinoAndreiRightHand);
    }

    /**
     * Partially update a alPacinoAndreiRightHand.
     *
     * @param alPacinoAndreiRightHand the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPacinoAndreiRightHand> partialUpdate(AlPacinoAndreiRightHand alPacinoAndreiRightHand) {
        LOG.debug("Request to partially update AlPacinoAndreiRightHand : {}", alPacinoAndreiRightHand);

        return alPacinoAndreiRightHandRepository
            .findById(alPacinoAndreiRightHand.getId())
            .map(existingAlPacinoAndreiRightHand -> {
                if (alPacinoAndreiRightHand.getName() != null) {
                    existingAlPacinoAndreiRightHand.setName(alPacinoAndreiRightHand.getName());
                }
                if (alPacinoAndreiRightHand.getIsDefault() != null) {
                    existingAlPacinoAndreiRightHand.setIsDefault(alPacinoAndreiRightHand.getIsDefault());
                }

                return existingAlPacinoAndreiRightHand;
            })
            .map(alPacinoAndreiRightHandRepository::save);
    }

    /**
     * Get one alPacinoAndreiRightHand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPacinoAndreiRightHand> findOne(Long id) {
        LOG.debug("Request to get AlPacinoAndreiRightHand : {}", id);
        return alPacinoAndreiRightHandRepository.findById(id);
    }

    /**
     * Delete the alPacinoAndreiRightHand by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPacinoAndreiRightHand : {}", id);
        alPacinoAndreiRightHandRepository.deleteById(id);
    }
}
