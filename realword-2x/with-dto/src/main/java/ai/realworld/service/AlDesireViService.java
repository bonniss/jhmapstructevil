package ai.realworld.service;

import ai.realworld.domain.AlDesireVi;
import ai.realworld.repository.AlDesireViRepository;
import ai.realworld.service.dto.AlDesireViDTO;
import ai.realworld.service.mapper.AlDesireViMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlDesireVi}.
 */
@Service
@Transactional
public class AlDesireViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlDesireViService.class);

    private final AlDesireViRepository alDesireViRepository;

    private final AlDesireViMapper alDesireViMapper;

    public AlDesireViService(AlDesireViRepository alDesireViRepository, AlDesireViMapper alDesireViMapper) {
        this.alDesireViRepository = alDesireViRepository;
        this.alDesireViMapper = alDesireViMapper;
    }

    /**
     * Save a alDesireVi.
     *
     * @param alDesireViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlDesireViDTO save(AlDesireViDTO alDesireViDTO) {
        LOG.debug("Request to save AlDesireVi : {}", alDesireViDTO);
        AlDesireVi alDesireVi = alDesireViMapper.toEntity(alDesireViDTO);
        alDesireVi = alDesireViRepository.save(alDesireVi);
        return alDesireViMapper.toDto(alDesireVi);
    }

    /**
     * Update a alDesireVi.
     *
     * @param alDesireViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlDesireViDTO update(AlDesireViDTO alDesireViDTO) {
        LOG.debug("Request to update AlDesireVi : {}", alDesireViDTO);
        AlDesireVi alDesireVi = alDesireViMapper.toEntity(alDesireViDTO);
        alDesireVi = alDesireViRepository.save(alDesireVi);
        return alDesireViMapper.toDto(alDesireVi);
    }

    /**
     * Partially update a alDesireVi.
     *
     * @param alDesireViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlDesireViDTO> partialUpdate(AlDesireViDTO alDesireViDTO) {
        LOG.debug("Request to partially update AlDesireVi : {}", alDesireViDTO);

        return alDesireViRepository
            .findById(alDesireViDTO.getId())
            .map(existingAlDesireVi -> {
                alDesireViMapper.partialUpdate(existingAlDesireVi, alDesireViDTO);

                return existingAlDesireVi;
            })
            .map(alDesireViRepository::save)
            .map(alDesireViMapper::toDto);
    }

    /**
     * Get one alDesireVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlDesireViDTO> findOne(UUID id) {
        LOG.debug("Request to get AlDesireVi : {}", id);
        return alDesireViRepository.findById(id).map(alDesireViMapper::toDto);
    }

    /**
     * Delete the alDesireVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlDesireVi : {}", id);
        alDesireViRepository.deleteById(id);
    }
}
