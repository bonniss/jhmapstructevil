package ai.realworld.service;

import ai.realworld.domain.AlMenity;
import ai.realworld.repository.AlMenityRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlMenity}.
 */
@Service
@Transactional
public class AlMenityService {

    private static final Logger LOG = LoggerFactory.getLogger(AlMenityService.class);

    private final AlMenityRepository alMenityRepository;

    public AlMenityService(AlMenityRepository alMenityRepository) {
        this.alMenityRepository = alMenityRepository;
    }

    /**
     * Save a alMenity.
     *
     * @param alMenity the entity to save.
     * @return the persisted entity.
     */
    public AlMenity save(AlMenity alMenity) {
        LOG.debug("Request to save AlMenity : {}", alMenity);
        return alMenityRepository.save(alMenity);
    }

    /**
     * Update a alMenity.
     *
     * @param alMenity the entity to save.
     * @return the persisted entity.
     */
    public AlMenity update(AlMenity alMenity) {
        LOG.debug("Request to update AlMenity : {}", alMenity);
        return alMenityRepository.save(alMenity);
    }

    /**
     * Partially update a alMenity.
     *
     * @param alMenity the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlMenity> partialUpdate(AlMenity alMenity) {
        LOG.debug("Request to partially update AlMenity : {}", alMenity);

        return alMenityRepository
            .findById(alMenity.getId())
            .map(existingAlMenity -> {
                if (alMenity.getName() != null) {
                    existingAlMenity.setName(alMenity.getName());
                }
                if (alMenity.getIconSvg() != null) {
                    existingAlMenity.setIconSvg(alMenity.getIconSvg());
                }
                if (alMenity.getPropertyType() != null) {
                    existingAlMenity.setPropertyType(alMenity.getPropertyType());
                }

                return existingAlMenity;
            })
            .map(alMenityRepository::save);
    }

    /**
     * Get one alMenity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlMenity> findOne(Long id) {
        LOG.debug("Request to get AlMenity : {}", id);
        return alMenityRepository.findById(id);
    }

    /**
     * Delete the alMenity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlMenity : {}", id);
        alMenityRepository.deleteById(id);
    }
}
