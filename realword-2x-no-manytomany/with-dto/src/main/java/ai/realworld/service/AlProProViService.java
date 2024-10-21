package ai.realworld.service;

import ai.realworld.domain.AlProProVi;
import ai.realworld.repository.AlProProViRepository;
import ai.realworld.service.dto.AlProProViDTO;
import ai.realworld.service.mapper.AlProProViMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlProProVi}.
 */
@Service
@Transactional
public class AlProProViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlProProViService.class);

    private final AlProProViRepository alProProViRepository;

    private final AlProProViMapper alProProViMapper;

    public AlProProViService(AlProProViRepository alProProViRepository, AlProProViMapper alProProViMapper) {
        this.alProProViRepository = alProProViRepository;
        this.alProProViMapper = alProProViMapper;
    }

    /**
     * Save a alProProVi.
     *
     * @param alProProViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlProProViDTO save(AlProProViDTO alProProViDTO) {
        LOG.debug("Request to save AlProProVi : {}", alProProViDTO);
        AlProProVi alProProVi = alProProViMapper.toEntity(alProProViDTO);
        alProProVi = alProProViRepository.save(alProProVi);
        return alProProViMapper.toDto(alProProVi);
    }

    /**
     * Update a alProProVi.
     *
     * @param alProProViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlProProViDTO update(AlProProViDTO alProProViDTO) {
        LOG.debug("Request to update AlProProVi : {}", alProProViDTO);
        AlProProVi alProProVi = alProProViMapper.toEntity(alProProViDTO);
        alProProVi = alProProViRepository.save(alProProVi);
        return alProProViMapper.toDto(alProProVi);
    }

    /**
     * Partially update a alProProVi.
     *
     * @param alProProViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlProProViDTO> partialUpdate(AlProProViDTO alProProViDTO) {
        LOG.debug("Request to partially update AlProProVi : {}", alProProViDTO);

        return alProProViRepository
            .findById(alProProViDTO.getId())
            .map(existingAlProProVi -> {
                alProProViMapper.partialUpdate(existingAlProProVi, alProProViDTO);

                return existingAlProProVi;
            })
            .map(alProProViRepository::save)
            .map(alProProViMapper::toDto);
    }

    /**
     * Get one alProProVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlProProViDTO> findOne(UUID id) {
        LOG.debug("Request to get AlProProVi : {}", id);
        return alProProViRepository.findById(id).map(alProProViMapper::toDto);
    }

    /**
     * Delete the alProProVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlProProVi : {}", id);
        alProProViRepository.deleteById(id);
    }
}
