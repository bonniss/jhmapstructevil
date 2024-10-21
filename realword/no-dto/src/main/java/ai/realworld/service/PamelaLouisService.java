package ai.realworld.service;

import ai.realworld.domain.PamelaLouis;
import ai.realworld.repository.PamelaLouisRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.PamelaLouis}.
 */
@Service
@Transactional
public class PamelaLouisService {

    private static final Logger LOG = LoggerFactory.getLogger(PamelaLouisService.class);

    private final PamelaLouisRepository pamelaLouisRepository;

    public PamelaLouisService(PamelaLouisRepository pamelaLouisRepository) {
        this.pamelaLouisRepository = pamelaLouisRepository;
    }

    /**
     * Save a pamelaLouis.
     *
     * @param pamelaLouis the entity to save.
     * @return the persisted entity.
     */
    public PamelaLouis save(PamelaLouis pamelaLouis) {
        LOG.debug("Request to save PamelaLouis : {}", pamelaLouis);
        return pamelaLouisRepository.save(pamelaLouis);
    }

    /**
     * Update a pamelaLouis.
     *
     * @param pamelaLouis the entity to save.
     * @return the persisted entity.
     */
    public PamelaLouis update(PamelaLouis pamelaLouis) {
        LOG.debug("Request to update PamelaLouis : {}", pamelaLouis);
        return pamelaLouisRepository.save(pamelaLouis);
    }

    /**
     * Partially update a pamelaLouis.
     *
     * @param pamelaLouis the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PamelaLouis> partialUpdate(PamelaLouis pamelaLouis) {
        LOG.debug("Request to partially update PamelaLouis : {}", pamelaLouis);

        return pamelaLouisRepository
            .findById(pamelaLouis.getId())
            .map(existingPamelaLouis -> {
                if (pamelaLouis.getName() != null) {
                    existingPamelaLouis.setName(pamelaLouis.getName());
                }
                if (pamelaLouis.getConfigJason() != null) {
                    existingPamelaLouis.setConfigJason(pamelaLouis.getConfigJason());
                }

                return existingPamelaLouis;
            })
            .map(pamelaLouisRepository::save);
    }

    /**
     * Get one pamelaLouis by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PamelaLouis> findOne(Long id) {
        LOG.debug("Request to get PamelaLouis : {}", id);
        return pamelaLouisRepository.findById(id);
    }

    /**
     * Delete the pamelaLouis by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PamelaLouis : {}", id);
        pamelaLouisRepository.deleteById(id);
    }
}
