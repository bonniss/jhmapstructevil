package ai.realworld.service;

import ai.realworld.domain.AlVueVueCondition;
import ai.realworld.repository.AlVueVueConditionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlVueVueCondition}.
 */
@Service
@Transactional
public class AlVueVueConditionService {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueConditionService.class);

    private final AlVueVueConditionRepository alVueVueConditionRepository;

    public AlVueVueConditionService(AlVueVueConditionRepository alVueVueConditionRepository) {
        this.alVueVueConditionRepository = alVueVueConditionRepository;
    }

    /**
     * Save a alVueVueCondition.
     *
     * @param alVueVueCondition the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueCondition save(AlVueVueCondition alVueVueCondition) {
        LOG.debug("Request to save AlVueVueCondition : {}", alVueVueCondition);
        return alVueVueConditionRepository.save(alVueVueCondition);
    }

    /**
     * Update a alVueVueCondition.
     *
     * @param alVueVueCondition the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueCondition update(AlVueVueCondition alVueVueCondition) {
        LOG.debug("Request to update AlVueVueCondition : {}", alVueVueCondition);
        return alVueVueConditionRepository.save(alVueVueCondition);
    }

    /**
     * Partially update a alVueVueCondition.
     *
     * @param alVueVueCondition the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlVueVueCondition> partialUpdate(AlVueVueCondition alVueVueCondition) {
        LOG.debug("Request to partially update AlVueVueCondition : {}", alVueVueCondition);

        return alVueVueConditionRepository
            .findById(alVueVueCondition.getId())
            .map(existingAlVueVueCondition -> {
                if (alVueVueCondition.getSubjectType() != null) {
                    existingAlVueVueCondition.setSubjectType(alVueVueCondition.getSubjectType());
                }
                if (alVueVueCondition.getSubject() != null) {
                    existingAlVueVueCondition.setSubject(alVueVueCondition.getSubject());
                }
                if (alVueVueCondition.getAction() != null) {
                    existingAlVueVueCondition.setAction(alVueVueCondition.getAction());
                }
                if (alVueVueCondition.getNote() != null) {
                    existingAlVueVueCondition.setNote(alVueVueCondition.getNote());
                }

                return existingAlVueVueCondition;
            })
            .map(alVueVueConditionRepository::save);
    }

    /**
     * Get one alVueVueCondition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlVueVueCondition> findOne(Long id) {
        LOG.debug("Request to get AlVueVueCondition : {}", id);
        return alVueVueConditionRepository.findById(id);
    }

    /**
     * Delete the alVueVueCondition by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlVueVueCondition : {}", id);
        alVueVueConditionRepository.deleteById(id);
    }
}
