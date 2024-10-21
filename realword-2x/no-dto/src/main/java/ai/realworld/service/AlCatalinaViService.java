package ai.realworld.service;

import ai.realworld.domain.AlCatalinaVi;
import ai.realworld.repository.AlCatalinaViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlCatalinaVi}.
 */
@Service
@Transactional
public class AlCatalinaViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlCatalinaViService.class);

    private final AlCatalinaViRepository alCatalinaViRepository;

    public AlCatalinaViService(AlCatalinaViRepository alCatalinaViRepository) {
        this.alCatalinaViRepository = alCatalinaViRepository;
    }

    /**
     * Save a alCatalinaVi.
     *
     * @param alCatalinaVi the entity to save.
     * @return the persisted entity.
     */
    public AlCatalinaVi save(AlCatalinaVi alCatalinaVi) {
        LOG.debug("Request to save AlCatalinaVi : {}", alCatalinaVi);
        return alCatalinaViRepository.save(alCatalinaVi);
    }

    /**
     * Update a alCatalinaVi.
     *
     * @param alCatalinaVi the entity to save.
     * @return the persisted entity.
     */
    public AlCatalinaVi update(AlCatalinaVi alCatalinaVi) {
        LOG.debug("Request to update AlCatalinaVi : {}", alCatalinaVi);
        return alCatalinaViRepository.save(alCatalinaVi);
    }

    /**
     * Partially update a alCatalinaVi.
     *
     * @param alCatalinaVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlCatalinaVi> partialUpdate(AlCatalinaVi alCatalinaVi) {
        LOG.debug("Request to partially update AlCatalinaVi : {}", alCatalinaVi);

        return alCatalinaViRepository
            .findById(alCatalinaVi.getId())
            .map(existingAlCatalinaVi -> {
                if (alCatalinaVi.getName() != null) {
                    existingAlCatalinaVi.setName(alCatalinaVi.getName());
                }
                if (alCatalinaVi.getDescription() != null) {
                    existingAlCatalinaVi.setDescription(alCatalinaVi.getDescription());
                }
                if (alCatalinaVi.getTreeDepth() != null) {
                    existingAlCatalinaVi.setTreeDepth(alCatalinaVi.getTreeDepth());
                }

                return existingAlCatalinaVi;
            })
            .map(alCatalinaViRepository::save);
    }

    /**
     * Get one alCatalinaVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlCatalinaVi> findOne(Long id) {
        LOG.debug("Request to get AlCatalinaVi : {}", id);
        return alCatalinaViRepository.findById(id);
    }

    /**
     * Delete the alCatalinaVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlCatalinaVi : {}", id);
        alCatalinaViRepository.deleteById(id);
    }
}
