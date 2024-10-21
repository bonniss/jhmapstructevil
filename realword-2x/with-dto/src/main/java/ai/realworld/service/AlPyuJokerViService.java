package ai.realworld.service;

import ai.realworld.domain.AlPyuJokerVi;
import ai.realworld.repository.AlPyuJokerViRepository;
import ai.realworld.service.dto.AlPyuJokerViDTO;
import ai.realworld.service.mapper.AlPyuJokerViMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPyuJokerVi}.
 */
@Service
@Transactional
public class AlPyuJokerViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuJokerViService.class);

    private final AlPyuJokerViRepository alPyuJokerViRepository;

    private final AlPyuJokerViMapper alPyuJokerViMapper;

    public AlPyuJokerViService(AlPyuJokerViRepository alPyuJokerViRepository, AlPyuJokerViMapper alPyuJokerViMapper) {
        this.alPyuJokerViRepository = alPyuJokerViRepository;
        this.alPyuJokerViMapper = alPyuJokerViMapper;
    }

    /**
     * Save a alPyuJokerVi.
     *
     * @param alPyuJokerViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPyuJokerViDTO save(AlPyuJokerViDTO alPyuJokerViDTO) {
        LOG.debug("Request to save AlPyuJokerVi : {}", alPyuJokerViDTO);
        AlPyuJokerVi alPyuJokerVi = alPyuJokerViMapper.toEntity(alPyuJokerViDTO);
        alPyuJokerVi = alPyuJokerViRepository.save(alPyuJokerVi);
        return alPyuJokerViMapper.toDto(alPyuJokerVi);
    }

    /**
     * Update a alPyuJokerVi.
     *
     * @param alPyuJokerViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPyuJokerViDTO update(AlPyuJokerViDTO alPyuJokerViDTO) {
        LOG.debug("Request to update AlPyuJokerVi : {}", alPyuJokerViDTO);
        AlPyuJokerVi alPyuJokerVi = alPyuJokerViMapper.toEntity(alPyuJokerViDTO);
        alPyuJokerVi = alPyuJokerViRepository.save(alPyuJokerVi);
        return alPyuJokerViMapper.toDto(alPyuJokerVi);
    }

    /**
     * Partially update a alPyuJokerVi.
     *
     * @param alPyuJokerViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPyuJokerViDTO> partialUpdate(AlPyuJokerViDTO alPyuJokerViDTO) {
        LOG.debug("Request to partially update AlPyuJokerVi : {}", alPyuJokerViDTO);

        return alPyuJokerViRepository
            .findById(alPyuJokerViDTO.getId())
            .map(existingAlPyuJokerVi -> {
                alPyuJokerViMapper.partialUpdate(existingAlPyuJokerVi, alPyuJokerViDTO);

                return existingAlPyuJokerVi;
            })
            .map(alPyuJokerViRepository::save)
            .map(alPyuJokerViMapper::toDto);
    }

    /**
     * Get all the alPyuJokerVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AlPyuJokerViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return alPyuJokerViRepository.findAllWithEagerRelationships(pageable).map(alPyuJokerViMapper::toDto);
    }

    /**
     * Get one alPyuJokerVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPyuJokerViDTO> findOne(UUID id) {
        LOG.debug("Request to get AlPyuJokerVi : {}", id);
        return alPyuJokerViRepository.findOneWithEagerRelationships(id).map(alPyuJokerViMapper::toDto);
    }

    /**
     * Delete the alPyuJokerVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlPyuJokerVi : {}", id);
        alPyuJokerViRepository.deleteById(id);
    }
}
