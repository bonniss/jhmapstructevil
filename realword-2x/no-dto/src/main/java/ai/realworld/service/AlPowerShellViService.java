package ai.realworld.service;

import ai.realworld.domain.AlPowerShellVi;
import ai.realworld.repository.AlPowerShellViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPowerShellVi}.
 */
@Service
@Transactional
public class AlPowerShellViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPowerShellViService.class);

    private final AlPowerShellViRepository alPowerShellViRepository;

    public AlPowerShellViService(AlPowerShellViRepository alPowerShellViRepository) {
        this.alPowerShellViRepository = alPowerShellViRepository;
    }

    /**
     * Save a alPowerShellVi.
     *
     * @param alPowerShellVi the entity to save.
     * @return the persisted entity.
     */
    public AlPowerShellVi save(AlPowerShellVi alPowerShellVi) {
        LOG.debug("Request to save AlPowerShellVi : {}", alPowerShellVi);
        return alPowerShellViRepository.save(alPowerShellVi);
    }

    /**
     * Update a alPowerShellVi.
     *
     * @param alPowerShellVi the entity to save.
     * @return the persisted entity.
     */
    public AlPowerShellVi update(AlPowerShellVi alPowerShellVi) {
        LOG.debug("Request to update AlPowerShellVi : {}", alPowerShellVi);
        return alPowerShellViRepository.save(alPowerShellVi);
    }

    /**
     * Partially update a alPowerShellVi.
     *
     * @param alPowerShellVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPowerShellVi> partialUpdate(AlPowerShellVi alPowerShellVi) {
        LOG.debug("Request to partially update AlPowerShellVi : {}", alPowerShellVi);

        return alPowerShellViRepository
            .findById(alPowerShellVi.getId())
            .map(existingAlPowerShellVi -> {
                if (alPowerShellVi.getValue() != null) {
                    existingAlPowerShellVi.setValue(alPowerShellVi.getValue());
                }

                return existingAlPowerShellVi;
            })
            .map(alPowerShellViRepository::save);
    }

    /**
     * Get one alPowerShellVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPowerShellVi> findOne(Long id) {
        LOG.debug("Request to get AlPowerShellVi : {}", id);
        return alPowerShellViRepository.findById(id);
    }

    /**
     * Delete the alPowerShellVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPowerShellVi : {}", id);
        alPowerShellViRepository.deleteById(id);
    }
}
