package ai.realworld.service;

import ai.realworld.domain.AlProty;
import ai.realworld.repository.AlProtyRepository;
import ai.realworld.service.dto.AlProtyDTO;
import ai.realworld.service.mapper.AlProtyMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlProty}.
 */
@Service
@Transactional
public class AlProtyService {

    private static final Logger LOG = LoggerFactory.getLogger(AlProtyService.class);

    private final AlProtyRepository alProtyRepository;

    private final AlProtyMapper alProtyMapper;

    public AlProtyService(AlProtyRepository alProtyRepository, AlProtyMapper alProtyMapper) {
        this.alProtyRepository = alProtyRepository;
        this.alProtyMapper = alProtyMapper;
    }

    /**
     * Save a alProty.
     *
     * @param alProtyDTO the entity to save.
     * @return the persisted entity.
     */
    public AlProtyDTO save(AlProtyDTO alProtyDTO) {
        LOG.debug("Request to save AlProty : {}", alProtyDTO);
        AlProty alProty = alProtyMapper.toEntity(alProtyDTO);
        alProty = alProtyRepository.save(alProty);
        return alProtyMapper.toDto(alProty);
    }

    /**
     * Update a alProty.
     *
     * @param alProtyDTO the entity to save.
     * @return the persisted entity.
     */
    public AlProtyDTO update(AlProtyDTO alProtyDTO) {
        LOG.debug("Request to update AlProty : {}", alProtyDTO);
        AlProty alProty = alProtyMapper.toEntity(alProtyDTO);
        alProty = alProtyRepository.save(alProty);
        return alProtyMapper.toDto(alProty);
    }

    /**
     * Partially update a alProty.
     *
     * @param alProtyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlProtyDTO> partialUpdate(AlProtyDTO alProtyDTO) {
        LOG.debug("Request to partially update AlProty : {}", alProtyDTO);

        return alProtyRepository
            .findById(alProtyDTO.getId())
            .map(existingAlProty -> {
                alProtyMapper.partialUpdate(existingAlProty, alProtyDTO);

                return existingAlProty;
            })
            .map(alProtyRepository::save)
            .map(alProtyMapper::toDto);
    }

    /**
     * Get all the alProties with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AlProtyDTO> findAllWithEagerRelationships(Pageable pageable) {
        return alProtyRepository.findAllWithEagerRelationships(pageable).map(alProtyMapper::toDto);
    }

    /**
     * Get one alProty by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlProtyDTO> findOne(UUID id) {
        LOG.debug("Request to get AlProty : {}", id);
        return alProtyRepository.findOneWithEagerRelationships(id).map(alProtyMapper::toDto);
    }

    /**
     * Delete the alProty by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlProty : {}", id);
        alProtyRepository.deleteById(id);
    }
}
