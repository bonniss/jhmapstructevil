package ai.realworld.service;

import ai.realworld.domain.AlPacinoAndreiRightHandVi;
import ai.realworld.repository.AlPacinoAndreiRightHandViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPacinoAndreiRightHandVi}.
 */
@Service
@Transactional
public class AlPacinoAndreiRightHandViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoAndreiRightHandViService.class);

    private final AlPacinoAndreiRightHandViRepository alPacinoAndreiRightHandViRepository;

    public AlPacinoAndreiRightHandViService(AlPacinoAndreiRightHandViRepository alPacinoAndreiRightHandViRepository) {
        this.alPacinoAndreiRightHandViRepository = alPacinoAndreiRightHandViRepository;
    }

    /**
     * Save a alPacinoAndreiRightHandVi.
     *
     * @param alPacinoAndreiRightHandVi the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoAndreiRightHandVi save(AlPacinoAndreiRightHandVi alPacinoAndreiRightHandVi) {
        LOG.debug("Request to save AlPacinoAndreiRightHandVi : {}", alPacinoAndreiRightHandVi);
        return alPacinoAndreiRightHandViRepository.save(alPacinoAndreiRightHandVi);
    }

    /**
     * Update a alPacinoAndreiRightHandVi.
     *
     * @param alPacinoAndreiRightHandVi the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoAndreiRightHandVi update(AlPacinoAndreiRightHandVi alPacinoAndreiRightHandVi) {
        LOG.debug("Request to update AlPacinoAndreiRightHandVi : {}", alPacinoAndreiRightHandVi);
        return alPacinoAndreiRightHandViRepository.save(alPacinoAndreiRightHandVi);
    }

    /**
     * Partially update a alPacinoAndreiRightHandVi.
     *
     * @param alPacinoAndreiRightHandVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPacinoAndreiRightHandVi> partialUpdate(AlPacinoAndreiRightHandVi alPacinoAndreiRightHandVi) {
        LOG.debug("Request to partially update AlPacinoAndreiRightHandVi : {}", alPacinoAndreiRightHandVi);

        return alPacinoAndreiRightHandViRepository
            .findById(alPacinoAndreiRightHandVi.getId())
            .map(existingAlPacinoAndreiRightHandVi -> {
                if (alPacinoAndreiRightHandVi.getName() != null) {
                    existingAlPacinoAndreiRightHandVi.setName(alPacinoAndreiRightHandVi.getName());
                }
                if (alPacinoAndreiRightHandVi.getIsDefault() != null) {
                    existingAlPacinoAndreiRightHandVi.setIsDefault(alPacinoAndreiRightHandVi.getIsDefault());
                }

                return existingAlPacinoAndreiRightHandVi;
            })
            .map(alPacinoAndreiRightHandViRepository::save);
    }

    /**
     * Get one alPacinoAndreiRightHandVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPacinoAndreiRightHandVi> findOne(Long id) {
        LOG.debug("Request to get AlPacinoAndreiRightHandVi : {}", id);
        return alPacinoAndreiRightHandViRepository.findById(id);
    }

    /**
     * Delete the alPacinoAndreiRightHandVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPacinoAndreiRightHandVi : {}", id);
        alPacinoAndreiRightHandViRepository.deleteById(id);
    }
}
