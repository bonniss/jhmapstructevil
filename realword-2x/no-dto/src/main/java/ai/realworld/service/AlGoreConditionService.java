package ai.realworld.service;

import ai.realworld.domain.AlGoreCondition;
import ai.realworld.repository.AlGoreConditionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlGoreCondition}.
 */
@Service
@Transactional
public class AlGoreConditionService {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreConditionService.class);

    private final AlGoreConditionRepository alGoreConditionRepository;

    public AlGoreConditionService(AlGoreConditionRepository alGoreConditionRepository) {
        this.alGoreConditionRepository = alGoreConditionRepository;
    }

    /**
     * Save a alGoreCondition.
     *
     * @param alGoreCondition the entity to save.
     * @return the persisted entity.
     */
    public AlGoreCondition save(AlGoreCondition alGoreCondition) {
        LOG.debug("Request to save AlGoreCondition : {}", alGoreCondition);
        return alGoreConditionRepository.save(alGoreCondition);
    }

    /**
     * Update a alGoreCondition.
     *
     * @param alGoreCondition the entity to save.
     * @return the persisted entity.
     */
    public AlGoreCondition update(AlGoreCondition alGoreCondition) {
        LOG.debug("Request to update AlGoreCondition : {}", alGoreCondition);
        return alGoreConditionRepository.save(alGoreCondition);
    }

    /**
     * Partially update a alGoreCondition.
     *
     * @param alGoreCondition the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlGoreCondition> partialUpdate(AlGoreCondition alGoreCondition) {
        LOG.debug("Request to partially update AlGoreCondition : {}", alGoreCondition);

        return alGoreConditionRepository
            .findById(alGoreCondition.getId())
            .map(existingAlGoreCondition -> {
                if (alGoreCondition.getSubjectType() != null) {
                    existingAlGoreCondition.setSubjectType(alGoreCondition.getSubjectType());
                }
                if (alGoreCondition.getSubject() != null) {
                    existingAlGoreCondition.setSubject(alGoreCondition.getSubject());
                }
                if (alGoreCondition.getAction() != null) {
                    existingAlGoreCondition.setAction(alGoreCondition.getAction());
                }
                if (alGoreCondition.getNote() != null) {
                    existingAlGoreCondition.setNote(alGoreCondition.getNote());
                }

                return existingAlGoreCondition;
            })
            .map(alGoreConditionRepository::save);
    }

    /**
     * Get one alGoreCondition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlGoreCondition> findOne(Long id) {
        LOG.debug("Request to get AlGoreCondition : {}", id);
        return alGoreConditionRepository.findById(id);
    }

    /**
     * Delete the alGoreCondition by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlGoreCondition : {}", id);
        alGoreConditionRepository.deleteById(id);
    }
}
