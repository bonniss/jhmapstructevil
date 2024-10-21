package ai.realworld.service;

import ai.realworld.domain.AlPowerShell;
import ai.realworld.repository.AlPowerShellRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPowerShell}.
 */
@Service
@Transactional
public class AlPowerShellService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPowerShellService.class);

    private final AlPowerShellRepository alPowerShellRepository;

    public AlPowerShellService(AlPowerShellRepository alPowerShellRepository) {
        this.alPowerShellRepository = alPowerShellRepository;
    }

    /**
     * Save a alPowerShell.
     *
     * @param alPowerShell the entity to save.
     * @return the persisted entity.
     */
    public AlPowerShell save(AlPowerShell alPowerShell) {
        LOG.debug("Request to save AlPowerShell : {}", alPowerShell);
        return alPowerShellRepository.save(alPowerShell);
    }

    /**
     * Update a alPowerShell.
     *
     * @param alPowerShell the entity to save.
     * @return the persisted entity.
     */
    public AlPowerShell update(AlPowerShell alPowerShell) {
        LOG.debug("Request to update AlPowerShell : {}", alPowerShell);
        return alPowerShellRepository.save(alPowerShell);
    }

    /**
     * Partially update a alPowerShell.
     *
     * @param alPowerShell the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPowerShell> partialUpdate(AlPowerShell alPowerShell) {
        LOG.debug("Request to partially update AlPowerShell : {}", alPowerShell);

        return alPowerShellRepository
            .findById(alPowerShell.getId())
            .map(existingAlPowerShell -> {
                if (alPowerShell.getValue() != null) {
                    existingAlPowerShell.setValue(alPowerShell.getValue());
                }

                return existingAlPowerShell;
            })
            .map(alPowerShellRepository::save);
    }

    /**
     * Get one alPowerShell by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPowerShell> findOne(Long id) {
        LOG.debug("Request to get AlPowerShell : {}", id);
        return alPowerShellRepository.findById(id);
    }

    /**
     * Delete the alPowerShell by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPowerShell : {}", id);
        alPowerShellRepository.deleteById(id);
    }
}
