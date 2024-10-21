package ai.realworld.service;

import ai.realworld.domain.AndreiRightHandVi;
import ai.realworld.repository.AndreiRightHandViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AndreiRightHandVi}.
 */
@Service
@Transactional
public class AndreiRightHandViService {

    private static final Logger LOG = LoggerFactory.getLogger(AndreiRightHandViService.class);

    private final AndreiRightHandViRepository andreiRightHandViRepository;

    public AndreiRightHandViService(AndreiRightHandViRepository andreiRightHandViRepository) {
        this.andreiRightHandViRepository = andreiRightHandViRepository;
    }

    /**
     * Save a andreiRightHandVi.
     *
     * @param andreiRightHandVi the entity to save.
     * @return the persisted entity.
     */
    public AndreiRightHandVi save(AndreiRightHandVi andreiRightHandVi) {
        LOG.debug("Request to save AndreiRightHandVi : {}", andreiRightHandVi);
        return andreiRightHandViRepository.save(andreiRightHandVi);
    }

    /**
     * Update a andreiRightHandVi.
     *
     * @param andreiRightHandVi the entity to save.
     * @return the persisted entity.
     */
    public AndreiRightHandVi update(AndreiRightHandVi andreiRightHandVi) {
        LOG.debug("Request to update AndreiRightHandVi : {}", andreiRightHandVi);
        return andreiRightHandViRepository.save(andreiRightHandVi);
    }

    /**
     * Partially update a andreiRightHandVi.
     *
     * @param andreiRightHandVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AndreiRightHandVi> partialUpdate(AndreiRightHandVi andreiRightHandVi) {
        LOG.debug("Request to partially update AndreiRightHandVi : {}", andreiRightHandVi);

        return andreiRightHandViRepository
            .findById(andreiRightHandVi.getId())
            .map(existingAndreiRightHandVi -> {
                if (andreiRightHandVi.getDetails() != null) {
                    existingAndreiRightHandVi.setDetails(andreiRightHandVi.getDetails());
                }
                if (andreiRightHandVi.getLat() != null) {
                    existingAndreiRightHandVi.setLat(andreiRightHandVi.getLat());
                }
                if (andreiRightHandVi.getLon() != null) {
                    existingAndreiRightHandVi.setLon(andreiRightHandVi.getLon());
                }

                return existingAndreiRightHandVi;
            })
            .map(andreiRightHandViRepository::save);
    }

    /**
     * Get one andreiRightHandVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AndreiRightHandVi> findOne(Long id) {
        LOG.debug("Request to get AndreiRightHandVi : {}", id);
        return andreiRightHandViRepository.findById(id);
    }

    /**
     * Delete the andreiRightHandVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AndreiRightHandVi : {}", id);
        andreiRightHandViRepository.deleteById(id);
    }
}
