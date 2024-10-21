package ai.realworld.service;

import ai.realworld.domain.AlVueVueCondition;
import ai.realworld.repository.AlVueVueConditionRepository;
import ai.realworld.service.dto.AlVueVueConditionDTO;
import ai.realworld.service.mapper.AlVueVueConditionMapper;
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

    private final AlVueVueConditionMapper alVueVueConditionMapper;

    public AlVueVueConditionService(
        AlVueVueConditionRepository alVueVueConditionRepository,
        AlVueVueConditionMapper alVueVueConditionMapper
    ) {
        this.alVueVueConditionRepository = alVueVueConditionRepository;
        this.alVueVueConditionMapper = alVueVueConditionMapper;
    }

    /**
     * Save a alVueVueCondition.
     *
     * @param alVueVueConditionDTO the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueConditionDTO save(AlVueVueConditionDTO alVueVueConditionDTO) {
        LOG.debug("Request to save AlVueVueCondition : {}", alVueVueConditionDTO);
        AlVueVueCondition alVueVueCondition = alVueVueConditionMapper.toEntity(alVueVueConditionDTO);
        alVueVueCondition = alVueVueConditionRepository.save(alVueVueCondition);
        return alVueVueConditionMapper.toDto(alVueVueCondition);
    }

    /**
     * Update a alVueVueCondition.
     *
     * @param alVueVueConditionDTO the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueConditionDTO update(AlVueVueConditionDTO alVueVueConditionDTO) {
        LOG.debug("Request to update AlVueVueCondition : {}", alVueVueConditionDTO);
        AlVueVueCondition alVueVueCondition = alVueVueConditionMapper.toEntity(alVueVueConditionDTO);
        alVueVueCondition = alVueVueConditionRepository.save(alVueVueCondition);
        return alVueVueConditionMapper.toDto(alVueVueCondition);
    }

    /**
     * Partially update a alVueVueCondition.
     *
     * @param alVueVueConditionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlVueVueConditionDTO> partialUpdate(AlVueVueConditionDTO alVueVueConditionDTO) {
        LOG.debug("Request to partially update AlVueVueCondition : {}", alVueVueConditionDTO);

        return alVueVueConditionRepository
            .findById(alVueVueConditionDTO.getId())
            .map(existingAlVueVueCondition -> {
                alVueVueConditionMapper.partialUpdate(existingAlVueVueCondition, alVueVueConditionDTO);

                return existingAlVueVueCondition;
            })
            .map(alVueVueConditionRepository::save)
            .map(alVueVueConditionMapper::toDto);
    }

    /**
     * Get one alVueVueCondition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlVueVueConditionDTO> findOne(Long id) {
        LOG.debug("Request to get AlVueVueCondition : {}", id);
        return alVueVueConditionRepository.findById(id).map(alVueVueConditionMapper::toDto);
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
