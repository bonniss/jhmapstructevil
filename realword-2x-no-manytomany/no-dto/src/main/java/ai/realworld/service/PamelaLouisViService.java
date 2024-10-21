package ai.realworld.service;

import ai.realworld.domain.PamelaLouisVi;
import ai.realworld.repository.PamelaLouisViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.PamelaLouisVi}.
 */
@Service
@Transactional
public class PamelaLouisViService {

    private static final Logger LOG = LoggerFactory.getLogger(PamelaLouisViService.class);

    private final PamelaLouisViRepository pamelaLouisViRepository;

    public PamelaLouisViService(PamelaLouisViRepository pamelaLouisViRepository) {
        this.pamelaLouisViRepository = pamelaLouisViRepository;
    }

    /**
     * Save a pamelaLouisVi.
     *
     * @param pamelaLouisVi the entity to save.
     * @return the persisted entity.
     */
    public PamelaLouisVi save(PamelaLouisVi pamelaLouisVi) {
        LOG.debug("Request to save PamelaLouisVi : {}", pamelaLouisVi);
        return pamelaLouisViRepository.save(pamelaLouisVi);
    }

    /**
     * Update a pamelaLouisVi.
     *
     * @param pamelaLouisVi the entity to save.
     * @return the persisted entity.
     */
    public PamelaLouisVi update(PamelaLouisVi pamelaLouisVi) {
        LOG.debug("Request to update PamelaLouisVi : {}", pamelaLouisVi);
        return pamelaLouisViRepository.save(pamelaLouisVi);
    }

    /**
     * Partially update a pamelaLouisVi.
     *
     * @param pamelaLouisVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PamelaLouisVi> partialUpdate(PamelaLouisVi pamelaLouisVi) {
        LOG.debug("Request to partially update PamelaLouisVi : {}", pamelaLouisVi);

        return pamelaLouisViRepository
            .findById(pamelaLouisVi.getId())
            .map(existingPamelaLouisVi -> {
                if (pamelaLouisVi.getName() != null) {
                    existingPamelaLouisVi.setName(pamelaLouisVi.getName());
                }
                if (pamelaLouisVi.getConfigJason() != null) {
                    existingPamelaLouisVi.setConfigJason(pamelaLouisVi.getConfigJason());
                }

                return existingPamelaLouisVi;
            })
            .map(pamelaLouisViRepository::save);
    }

    /**
     * Get one pamelaLouisVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PamelaLouisVi> findOne(Long id) {
        LOG.debug("Request to get PamelaLouisVi : {}", id);
        return pamelaLouisViRepository.findById(id);
    }

    /**
     * Delete the pamelaLouisVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PamelaLouisVi : {}", id);
        pamelaLouisViRepository.deleteById(id);
    }
}
