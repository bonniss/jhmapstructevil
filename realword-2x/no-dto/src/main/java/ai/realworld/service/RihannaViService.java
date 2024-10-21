package ai.realworld.service;

import ai.realworld.domain.RihannaVi;
import ai.realworld.repository.RihannaViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.RihannaVi}.
 */
@Service
@Transactional
public class RihannaViService {

    private static final Logger LOG = LoggerFactory.getLogger(RihannaViService.class);

    private final RihannaViRepository rihannaViRepository;

    public RihannaViService(RihannaViRepository rihannaViRepository) {
        this.rihannaViRepository = rihannaViRepository;
    }

    /**
     * Save a rihannaVi.
     *
     * @param rihannaVi the entity to save.
     * @return the persisted entity.
     */
    public RihannaVi save(RihannaVi rihannaVi) {
        LOG.debug("Request to save RihannaVi : {}", rihannaVi);
        return rihannaViRepository.save(rihannaVi);
    }

    /**
     * Update a rihannaVi.
     *
     * @param rihannaVi the entity to save.
     * @return the persisted entity.
     */
    public RihannaVi update(RihannaVi rihannaVi) {
        LOG.debug("Request to update RihannaVi : {}", rihannaVi);
        return rihannaViRepository.save(rihannaVi);
    }

    /**
     * Partially update a rihannaVi.
     *
     * @param rihannaVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RihannaVi> partialUpdate(RihannaVi rihannaVi) {
        LOG.debug("Request to partially update RihannaVi : {}", rihannaVi);

        return rihannaViRepository
            .findById(rihannaVi.getId())
            .map(existingRihannaVi -> {
                if (rihannaVi.getName() != null) {
                    existingRihannaVi.setName(rihannaVi.getName());
                }
                if (rihannaVi.getDescription() != null) {
                    existingRihannaVi.setDescription(rihannaVi.getDescription());
                }
                if (rihannaVi.getPermissionGridJason() != null) {
                    existingRihannaVi.setPermissionGridJason(rihannaVi.getPermissionGridJason());
                }

                return existingRihannaVi;
            })
            .map(rihannaViRepository::save);
    }

    /**
     * Get one rihannaVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RihannaVi> findOne(Long id) {
        LOG.debug("Request to get RihannaVi : {}", id);
        return rihannaViRepository.findById(id);
    }

    /**
     * Delete the rihannaVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RihannaVi : {}", id);
        rihannaViRepository.deleteById(id);
    }
}
