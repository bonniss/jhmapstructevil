package ai.realworld.service;

import ai.realworld.domain.AlVueVueViCondition;
import ai.realworld.repository.AlVueVueViConditionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlVueVueViCondition}.
 */
@Service
@Transactional
public class AlVueVueViConditionService {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueViConditionService.class);

    private final AlVueVueViConditionRepository alVueVueViConditionRepository;

    public AlVueVueViConditionService(AlVueVueViConditionRepository alVueVueViConditionRepository) {
        this.alVueVueViConditionRepository = alVueVueViConditionRepository;
    }

    /**
     * Save a alVueVueViCondition.
     *
     * @param alVueVueViCondition the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueViCondition save(AlVueVueViCondition alVueVueViCondition) {
        LOG.debug("Request to save AlVueVueViCondition : {}", alVueVueViCondition);
        return alVueVueViConditionRepository.save(alVueVueViCondition);
    }

    /**
     * Update a alVueVueViCondition.
     *
     * @param alVueVueViCondition the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueViCondition update(AlVueVueViCondition alVueVueViCondition) {
        LOG.debug("Request to update AlVueVueViCondition : {}", alVueVueViCondition);
        return alVueVueViConditionRepository.save(alVueVueViCondition);
    }

    /**
     * Partially update a alVueVueViCondition.
     *
     * @param alVueVueViCondition the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlVueVueViCondition> partialUpdate(AlVueVueViCondition alVueVueViCondition) {
        LOG.debug("Request to partially update AlVueVueViCondition : {}", alVueVueViCondition);

        return alVueVueViConditionRepository
            .findById(alVueVueViCondition.getId())
            .map(existingAlVueVueViCondition -> {
                if (alVueVueViCondition.getSubjectType() != null) {
                    existingAlVueVueViCondition.setSubjectType(alVueVueViCondition.getSubjectType());
                }
                if (alVueVueViCondition.getSubject() != null) {
                    existingAlVueVueViCondition.setSubject(alVueVueViCondition.getSubject());
                }
                if (alVueVueViCondition.getAction() != null) {
                    existingAlVueVueViCondition.setAction(alVueVueViCondition.getAction());
                }
                if (alVueVueViCondition.getNote() != null) {
                    existingAlVueVueViCondition.setNote(alVueVueViCondition.getNote());
                }

                return existingAlVueVueViCondition;
            })
            .map(alVueVueViConditionRepository::save);
    }

    /**
     * Get one alVueVueViCondition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlVueVueViCondition> findOne(Long id) {
        LOG.debug("Request to get AlVueVueViCondition : {}", id);
        return alVueVueViConditionRepository.findById(id);
    }

    /**
     * Delete the alVueVueViCondition by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlVueVueViCondition : {}", id);
        alVueVueViConditionRepository.deleteById(id);
    }
}
