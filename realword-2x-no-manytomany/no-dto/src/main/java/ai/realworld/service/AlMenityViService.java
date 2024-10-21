package ai.realworld.service;

import ai.realworld.domain.AlMenityVi;
import ai.realworld.repository.AlMenityViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlMenityVi}.
 */
@Service
@Transactional
public class AlMenityViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlMenityViService.class);

    private final AlMenityViRepository alMenityViRepository;

    public AlMenityViService(AlMenityViRepository alMenityViRepository) {
        this.alMenityViRepository = alMenityViRepository;
    }

    /**
     * Save a alMenityVi.
     *
     * @param alMenityVi the entity to save.
     * @return the persisted entity.
     */
    public AlMenityVi save(AlMenityVi alMenityVi) {
        LOG.debug("Request to save AlMenityVi : {}", alMenityVi);
        return alMenityViRepository.save(alMenityVi);
    }

    /**
     * Update a alMenityVi.
     *
     * @param alMenityVi the entity to save.
     * @return the persisted entity.
     */
    public AlMenityVi update(AlMenityVi alMenityVi) {
        LOG.debug("Request to update AlMenityVi : {}", alMenityVi);
        return alMenityViRepository.save(alMenityVi);
    }

    /**
     * Partially update a alMenityVi.
     *
     * @param alMenityVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlMenityVi> partialUpdate(AlMenityVi alMenityVi) {
        LOG.debug("Request to partially update AlMenityVi : {}", alMenityVi);

        return alMenityViRepository
            .findById(alMenityVi.getId())
            .map(existingAlMenityVi -> {
                if (alMenityVi.getName() != null) {
                    existingAlMenityVi.setName(alMenityVi.getName());
                }
                if (alMenityVi.getIconSvg() != null) {
                    existingAlMenityVi.setIconSvg(alMenityVi.getIconSvg());
                }
                if (alMenityVi.getPropertyType() != null) {
                    existingAlMenityVi.setPropertyType(alMenityVi.getPropertyType());
                }

                return existingAlMenityVi;
            })
            .map(alMenityViRepository::save);
    }

    /**
     * Get one alMenityVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlMenityVi> findOne(Long id) {
        LOG.debug("Request to get AlMenityVi : {}", id);
        return alMenityViRepository.findById(id);
    }

    /**
     * Delete the alMenityVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlMenityVi : {}", id);
        alMenityViRepository.deleteById(id);
    }
}
