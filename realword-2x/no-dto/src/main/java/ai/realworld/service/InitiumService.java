package ai.realworld.service;

import ai.realworld.domain.Initium;
import ai.realworld.repository.InitiumRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.Initium}.
 */
@Service
@Transactional
public class InitiumService {

    private static final Logger LOG = LoggerFactory.getLogger(InitiumService.class);

    private final InitiumRepository initiumRepository;

    public InitiumService(InitiumRepository initiumRepository) {
        this.initiumRepository = initiumRepository;
    }

    /**
     * Save a initium.
     *
     * @param initium the entity to save.
     * @return the persisted entity.
     */
    public Initium save(Initium initium) {
        LOG.debug("Request to save Initium : {}", initium);
        return initiumRepository.save(initium);
    }

    /**
     * Update a initium.
     *
     * @param initium the entity to save.
     * @return the persisted entity.
     */
    public Initium update(Initium initium) {
        LOG.debug("Request to update Initium : {}", initium);
        return initiumRepository.save(initium);
    }

    /**
     * Partially update a initium.
     *
     * @param initium the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Initium> partialUpdate(Initium initium) {
        LOG.debug("Request to partially update Initium : {}", initium);

        return initiumRepository
            .findById(initium.getId())
            .map(existingInitium -> {
                if (initium.getName() != null) {
                    existingInitium.setName(initium.getName());
                }
                if (initium.getSlug() != null) {
                    existingInitium.setSlug(initium.getSlug());
                }
                if (initium.getDescription() != null) {
                    existingInitium.setDescription(initium.getDescription());
                }
                if (initium.getIsJelloSupported() != null) {
                    existingInitium.setIsJelloSupported(initium.getIsJelloSupported());
                }

                return existingInitium;
            })
            .map(initiumRepository::save);
    }

    /**
     * Get one initium by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Initium> findOne(Long id) {
        LOG.debug("Request to get Initium : {}", id);
        return initiumRepository.findById(id);
    }

    /**
     * Delete the initium by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Initium : {}", id);
        initiumRepository.deleteById(id);
    }
}
