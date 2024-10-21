package ai.realworld.service;

import ai.realworld.domain.AlVueVueViCondition;
import ai.realworld.repository.AlVueVueViConditionRepository;
import ai.realworld.service.dto.AlVueVueViConditionDTO;
import ai.realworld.service.mapper.AlVueVueViConditionMapper;
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

    private final AlVueVueViConditionMapper alVueVueViConditionMapper;

    public AlVueVueViConditionService(
        AlVueVueViConditionRepository alVueVueViConditionRepository,
        AlVueVueViConditionMapper alVueVueViConditionMapper
    ) {
        this.alVueVueViConditionRepository = alVueVueViConditionRepository;
        this.alVueVueViConditionMapper = alVueVueViConditionMapper;
    }

    /**
     * Save a alVueVueViCondition.
     *
     * @param alVueVueViConditionDTO the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueViConditionDTO save(AlVueVueViConditionDTO alVueVueViConditionDTO) {
        LOG.debug("Request to save AlVueVueViCondition : {}", alVueVueViConditionDTO);
        AlVueVueViCondition alVueVueViCondition = alVueVueViConditionMapper.toEntity(alVueVueViConditionDTO);
        alVueVueViCondition = alVueVueViConditionRepository.save(alVueVueViCondition);
        return alVueVueViConditionMapper.toDto(alVueVueViCondition);
    }

    /**
     * Update a alVueVueViCondition.
     *
     * @param alVueVueViConditionDTO the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueViConditionDTO update(AlVueVueViConditionDTO alVueVueViConditionDTO) {
        LOG.debug("Request to update AlVueVueViCondition : {}", alVueVueViConditionDTO);
        AlVueVueViCondition alVueVueViCondition = alVueVueViConditionMapper.toEntity(alVueVueViConditionDTO);
        alVueVueViCondition = alVueVueViConditionRepository.save(alVueVueViCondition);
        return alVueVueViConditionMapper.toDto(alVueVueViCondition);
    }

    /**
     * Partially update a alVueVueViCondition.
     *
     * @param alVueVueViConditionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlVueVueViConditionDTO> partialUpdate(AlVueVueViConditionDTO alVueVueViConditionDTO) {
        LOG.debug("Request to partially update AlVueVueViCondition : {}", alVueVueViConditionDTO);

        return alVueVueViConditionRepository
            .findById(alVueVueViConditionDTO.getId())
            .map(existingAlVueVueViCondition -> {
                alVueVueViConditionMapper.partialUpdate(existingAlVueVueViCondition, alVueVueViConditionDTO);

                return existingAlVueVueViCondition;
            })
            .map(alVueVueViConditionRepository::save)
            .map(alVueVueViConditionMapper::toDto);
    }

    /**
     * Get one alVueVueViCondition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlVueVueViConditionDTO> findOne(Long id) {
        LOG.debug("Request to get AlVueVueViCondition : {}", id);
        return alVueVueViConditionRepository.findById(id).map(alVueVueViConditionMapper::toDto);
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
