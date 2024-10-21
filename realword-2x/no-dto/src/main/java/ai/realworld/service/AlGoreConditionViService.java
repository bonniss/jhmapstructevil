package ai.realworld.service;

import ai.realworld.domain.AlGoreConditionVi;
import ai.realworld.repository.AlGoreConditionViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlGoreConditionVi}.
 */
@Service
@Transactional
public class AlGoreConditionViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreConditionViService.class);

    private final AlGoreConditionViRepository alGoreConditionViRepository;

    public AlGoreConditionViService(AlGoreConditionViRepository alGoreConditionViRepository) {
        this.alGoreConditionViRepository = alGoreConditionViRepository;
    }

    /**
     * Save a alGoreConditionVi.
     *
     * @param alGoreConditionVi the entity to save.
     * @return the persisted entity.
     */
    public AlGoreConditionVi save(AlGoreConditionVi alGoreConditionVi) {
        LOG.debug("Request to save AlGoreConditionVi : {}", alGoreConditionVi);
        return alGoreConditionViRepository.save(alGoreConditionVi);
    }

    /**
     * Update a alGoreConditionVi.
     *
     * @param alGoreConditionVi the entity to save.
     * @return the persisted entity.
     */
    public AlGoreConditionVi update(AlGoreConditionVi alGoreConditionVi) {
        LOG.debug("Request to update AlGoreConditionVi : {}", alGoreConditionVi);
        return alGoreConditionViRepository.save(alGoreConditionVi);
    }

    /**
     * Partially update a alGoreConditionVi.
     *
     * @param alGoreConditionVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlGoreConditionVi> partialUpdate(AlGoreConditionVi alGoreConditionVi) {
        LOG.debug("Request to partially update AlGoreConditionVi : {}", alGoreConditionVi);

        return alGoreConditionViRepository
            .findById(alGoreConditionVi.getId())
            .map(existingAlGoreConditionVi -> {
                if (alGoreConditionVi.getSubjectType() != null) {
                    existingAlGoreConditionVi.setSubjectType(alGoreConditionVi.getSubjectType());
                }
                if (alGoreConditionVi.getSubject() != null) {
                    existingAlGoreConditionVi.setSubject(alGoreConditionVi.getSubject());
                }
                if (alGoreConditionVi.getAction() != null) {
                    existingAlGoreConditionVi.setAction(alGoreConditionVi.getAction());
                }
                if (alGoreConditionVi.getNote() != null) {
                    existingAlGoreConditionVi.setNote(alGoreConditionVi.getNote());
                }

                return existingAlGoreConditionVi;
            })
            .map(alGoreConditionViRepository::save);
    }

    /**
     * Get one alGoreConditionVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlGoreConditionVi> findOne(Long id) {
        LOG.debug("Request to get AlGoreConditionVi : {}", id);
        return alGoreConditionViRepository.findById(id);
    }

    /**
     * Delete the alGoreConditionVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlGoreConditionVi : {}", id);
        alGoreConditionViRepository.deleteById(id);
    }
}
