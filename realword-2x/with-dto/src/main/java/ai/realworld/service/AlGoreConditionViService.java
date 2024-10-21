package ai.realworld.service;

import ai.realworld.domain.AlGoreConditionVi;
import ai.realworld.repository.AlGoreConditionViRepository;
import ai.realworld.service.dto.AlGoreConditionViDTO;
import ai.realworld.service.mapper.AlGoreConditionViMapper;
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

    private final AlGoreConditionViMapper alGoreConditionViMapper;

    public AlGoreConditionViService(
        AlGoreConditionViRepository alGoreConditionViRepository,
        AlGoreConditionViMapper alGoreConditionViMapper
    ) {
        this.alGoreConditionViRepository = alGoreConditionViRepository;
        this.alGoreConditionViMapper = alGoreConditionViMapper;
    }

    /**
     * Save a alGoreConditionVi.
     *
     * @param alGoreConditionViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlGoreConditionViDTO save(AlGoreConditionViDTO alGoreConditionViDTO) {
        LOG.debug("Request to save AlGoreConditionVi : {}", alGoreConditionViDTO);
        AlGoreConditionVi alGoreConditionVi = alGoreConditionViMapper.toEntity(alGoreConditionViDTO);
        alGoreConditionVi = alGoreConditionViRepository.save(alGoreConditionVi);
        return alGoreConditionViMapper.toDto(alGoreConditionVi);
    }

    /**
     * Update a alGoreConditionVi.
     *
     * @param alGoreConditionViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlGoreConditionViDTO update(AlGoreConditionViDTO alGoreConditionViDTO) {
        LOG.debug("Request to update AlGoreConditionVi : {}", alGoreConditionViDTO);
        AlGoreConditionVi alGoreConditionVi = alGoreConditionViMapper.toEntity(alGoreConditionViDTO);
        alGoreConditionVi = alGoreConditionViRepository.save(alGoreConditionVi);
        return alGoreConditionViMapper.toDto(alGoreConditionVi);
    }

    /**
     * Partially update a alGoreConditionVi.
     *
     * @param alGoreConditionViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlGoreConditionViDTO> partialUpdate(AlGoreConditionViDTO alGoreConditionViDTO) {
        LOG.debug("Request to partially update AlGoreConditionVi : {}", alGoreConditionViDTO);

        return alGoreConditionViRepository
            .findById(alGoreConditionViDTO.getId())
            .map(existingAlGoreConditionVi -> {
                alGoreConditionViMapper.partialUpdate(existingAlGoreConditionVi, alGoreConditionViDTO);

                return existingAlGoreConditionVi;
            })
            .map(alGoreConditionViRepository::save)
            .map(alGoreConditionViMapper::toDto);
    }

    /**
     * Get one alGoreConditionVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlGoreConditionViDTO> findOne(Long id) {
        LOG.debug("Request to get AlGoreConditionVi : {}", id);
        return alGoreConditionViRepository.findById(id).map(alGoreConditionViMapper::toDto);
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
