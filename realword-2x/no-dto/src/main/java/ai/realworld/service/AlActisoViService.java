package ai.realworld.service;

import ai.realworld.domain.AlActisoVi;
import ai.realworld.repository.AlActisoViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlActisoVi}.
 */
@Service
@Transactional
public class AlActisoViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlActisoViService.class);

    private final AlActisoViRepository alActisoViRepository;

    public AlActisoViService(AlActisoViRepository alActisoViRepository) {
        this.alActisoViRepository = alActisoViRepository;
    }

    /**
     * Save a alActisoVi.
     *
     * @param alActisoVi the entity to save.
     * @return the persisted entity.
     */
    public AlActisoVi save(AlActisoVi alActisoVi) {
        LOG.debug("Request to save AlActisoVi : {}", alActisoVi);
        return alActisoViRepository.save(alActisoVi);
    }

    /**
     * Update a alActisoVi.
     *
     * @param alActisoVi the entity to save.
     * @return the persisted entity.
     */
    public AlActisoVi update(AlActisoVi alActisoVi) {
        LOG.debug("Request to update AlActisoVi : {}", alActisoVi);
        return alActisoViRepository.save(alActisoVi);
    }

    /**
     * Partially update a alActisoVi.
     *
     * @param alActisoVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlActisoVi> partialUpdate(AlActisoVi alActisoVi) {
        LOG.debug("Request to partially update AlActisoVi : {}", alActisoVi);

        return alActisoViRepository
            .findById(alActisoVi.getId())
            .map(existingAlActisoVi -> {
                if (alActisoVi.getKey() != null) {
                    existingAlActisoVi.setKey(alActisoVi.getKey());
                }
                if (alActisoVi.getValueJason() != null) {
                    existingAlActisoVi.setValueJason(alActisoVi.getValueJason());
                }

                return existingAlActisoVi;
            })
            .map(alActisoViRepository::save);
    }

    /**
     * Get one alActisoVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlActisoVi> findOne(Long id) {
        LOG.debug("Request to get AlActisoVi : {}", id);
        return alActisoViRepository.findById(id);
    }

    /**
     * Delete the alActisoVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlActisoVi : {}", id);
        alActisoViRepository.deleteById(id);
    }
}
