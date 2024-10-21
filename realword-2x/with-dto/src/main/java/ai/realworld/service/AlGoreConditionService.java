package ai.realworld.service;

import ai.realworld.domain.AlGoreCondition;
import ai.realworld.repository.AlGoreConditionRepository;
import ai.realworld.service.dto.AlGoreConditionDTO;
import ai.realworld.service.mapper.AlGoreConditionMapper;
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

    private final AlGoreConditionMapper alGoreConditionMapper;

    public AlGoreConditionService(AlGoreConditionRepository alGoreConditionRepository, AlGoreConditionMapper alGoreConditionMapper) {
        this.alGoreConditionRepository = alGoreConditionRepository;
        this.alGoreConditionMapper = alGoreConditionMapper;
    }

    /**
     * Save a alGoreCondition.
     *
     * @param alGoreConditionDTO the entity to save.
     * @return the persisted entity.
     */
    public AlGoreConditionDTO save(AlGoreConditionDTO alGoreConditionDTO) {
        LOG.debug("Request to save AlGoreCondition : {}", alGoreConditionDTO);
        AlGoreCondition alGoreCondition = alGoreConditionMapper.toEntity(alGoreConditionDTO);
        alGoreCondition = alGoreConditionRepository.save(alGoreCondition);
        return alGoreConditionMapper.toDto(alGoreCondition);
    }

    /**
     * Update a alGoreCondition.
     *
     * @param alGoreConditionDTO the entity to save.
     * @return the persisted entity.
     */
    public AlGoreConditionDTO update(AlGoreConditionDTO alGoreConditionDTO) {
        LOG.debug("Request to update AlGoreCondition : {}", alGoreConditionDTO);
        AlGoreCondition alGoreCondition = alGoreConditionMapper.toEntity(alGoreConditionDTO);
        alGoreCondition = alGoreConditionRepository.save(alGoreCondition);
        return alGoreConditionMapper.toDto(alGoreCondition);
    }

    /**
     * Partially update a alGoreCondition.
     *
     * @param alGoreConditionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlGoreConditionDTO> partialUpdate(AlGoreConditionDTO alGoreConditionDTO) {
        LOG.debug("Request to partially update AlGoreCondition : {}", alGoreConditionDTO);

        return alGoreConditionRepository
            .findById(alGoreConditionDTO.getId())
            .map(existingAlGoreCondition -> {
                alGoreConditionMapper.partialUpdate(existingAlGoreCondition, alGoreConditionDTO);

                return existingAlGoreCondition;
            })
            .map(alGoreConditionRepository::save)
            .map(alGoreConditionMapper::toDto);
    }

    /**
     * Get one alGoreCondition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlGoreConditionDTO> findOne(Long id) {
        LOG.debug("Request to get AlGoreCondition : {}", id);
        return alGoreConditionRepository.findById(id).map(alGoreConditionMapper::toDto);
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
