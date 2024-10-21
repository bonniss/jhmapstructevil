package ai.realworld.service;

import ai.realworld.domain.InitiumVi;
import ai.realworld.repository.InitiumViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.InitiumVi}.
 */
@Service
@Transactional
public class InitiumViService {

    private static final Logger LOG = LoggerFactory.getLogger(InitiumViService.class);

    private final InitiumViRepository initiumViRepository;

    public InitiumViService(InitiumViRepository initiumViRepository) {
        this.initiumViRepository = initiumViRepository;
    }

    /**
     * Save a initiumVi.
     *
     * @param initiumVi the entity to save.
     * @return the persisted entity.
     */
    public InitiumVi save(InitiumVi initiumVi) {
        LOG.debug("Request to save InitiumVi : {}", initiumVi);
        return initiumViRepository.save(initiumVi);
    }

    /**
     * Update a initiumVi.
     *
     * @param initiumVi the entity to save.
     * @return the persisted entity.
     */
    public InitiumVi update(InitiumVi initiumVi) {
        LOG.debug("Request to update InitiumVi : {}", initiumVi);
        return initiumViRepository.save(initiumVi);
    }

    /**
     * Partially update a initiumVi.
     *
     * @param initiumVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InitiumVi> partialUpdate(InitiumVi initiumVi) {
        LOG.debug("Request to partially update InitiumVi : {}", initiumVi);

        return initiumViRepository
            .findById(initiumVi.getId())
            .map(existingInitiumVi -> {
                if (initiumVi.getName() != null) {
                    existingInitiumVi.setName(initiumVi.getName());
                }
                if (initiumVi.getSlug() != null) {
                    existingInitiumVi.setSlug(initiumVi.getSlug());
                }
                if (initiumVi.getDescription() != null) {
                    existingInitiumVi.setDescription(initiumVi.getDescription());
                }
                if (initiumVi.getIsJelloSupported() != null) {
                    existingInitiumVi.setIsJelloSupported(initiumVi.getIsJelloSupported());
                }

                return existingInitiumVi;
            })
            .map(initiumViRepository::save);
    }

    /**
     * Get one initiumVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InitiumVi> findOne(Long id) {
        LOG.debug("Request to get InitiumVi : {}", id);
        return initiumViRepository.findById(id);
    }

    /**
     * Delete the initiumVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InitiumVi : {}", id);
        initiumViRepository.deleteById(id);
    }
}
