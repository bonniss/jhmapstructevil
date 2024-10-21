package ai.realworld.service;

import ai.realworld.domain.AlProtyVi;
import ai.realworld.repository.AlProtyViRepository;
import ai.realworld.service.dto.AlProtyViDTO;
import ai.realworld.service.mapper.AlProtyViMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlProtyVi}.
 */
@Service
@Transactional
public class AlProtyViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlProtyViService.class);

    private final AlProtyViRepository alProtyViRepository;

    private final AlProtyViMapper alProtyViMapper;

    public AlProtyViService(AlProtyViRepository alProtyViRepository, AlProtyViMapper alProtyViMapper) {
        this.alProtyViRepository = alProtyViRepository;
        this.alProtyViMapper = alProtyViMapper;
    }

    /**
     * Save a alProtyVi.
     *
     * @param alProtyViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlProtyViDTO save(AlProtyViDTO alProtyViDTO) {
        LOG.debug("Request to save AlProtyVi : {}", alProtyViDTO);
        AlProtyVi alProtyVi = alProtyViMapper.toEntity(alProtyViDTO);
        alProtyVi = alProtyViRepository.save(alProtyVi);
        return alProtyViMapper.toDto(alProtyVi);
    }

    /**
     * Update a alProtyVi.
     *
     * @param alProtyViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlProtyViDTO update(AlProtyViDTO alProtyViDTO) {
        LOG.debug("Request to update AlProtyVi : {}", alProtyViDTO);
        AlProtyVi alProtyVi = alProtyViMapper.toEntity(alProtyViDTO);
        alProtyVi = alProtyViRepository.save(alProtyVi);
        return alProtyViMapper.toDto(alProtyVi);
    }

    /**
     * Partially update a alProtyVi.
     *
     * @param alProtyViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlProtyViDTO> partialUpdate(AlProtyViDTO alProtyViDTO) {
        LOG.debug("Request to partially update AlProtyVi : {}", alProtyViDTO);

        return alProtyViRepository
            .findById(alProtyViDTO.getId())
            .map(existingAlProtyVi -> {
                alProtyViMapper.partialUpdate(existingAlProtyVi, alProtyViDTO);

                return existingAlProtyVi;
            })
            .map(alProtyViRepository::save)
            .map(alProtyViMapper::toDto);
    }

    /**
     * Get all the alProtyVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AlProtyViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return alProtyViRepository.findAllWithEagerRelationships(pageable).map(alProtyViMapper::toDto);
    }

    /**
     * Get one alProtyVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlProtyViDTO> findOne(UUID id) {
        LOG.debug("Request to get AlProtyVi : {}", id);
        return alProtyViRepository.findOneWithEagerRelationships(id).map(alProtyViMapper::toDto);
    }

    /**
     * Delete the alProtyVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlProtyVi : {}", id);
        alProtyViRepository.deleteById(id);
    }
}
