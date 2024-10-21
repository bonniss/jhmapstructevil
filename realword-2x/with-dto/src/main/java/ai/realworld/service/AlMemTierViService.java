package ai.realworld.service;

import ai.realworld.domain.AlMemTierVi;
import ai.realworld.repository.AlMemTierViRepository;
import ai.realworld.service.dto.AlMemTierViDTO;
import ai.realworld.service.mapper.AlMemTierViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlMemTierVi}.
 */
@Service
@Transactional
public class AlMemTierViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlMemTierViService.class);

    private final AlMemTierViRepository alMemTierViRepository;

    private final AlMemTierViMapper alMemTierViMapper;

    public AlMemTierViService(AlMemTierViRepository alMemTierViRepository, AlMemTierViMapper alMemTierViMapper) {
        this.alMemTierViRepository = alMemTierViRepository;
        this.alMemTierViMapper = alMemTierViMapper;
    }

    /**
     * Save a alMemTierVi.
     *
     * @param alMemTierViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlMemTierViDTO save(AlMemTierViDTO alMemTierViDTO) {
        LOG.debug("Request to save AlMemTierVi : {}", alMemTierViDTO);
        AlMemTierVi alMemTierVi = alMemTierViMapper.toEntity(alMemTierViDTO);
        alMemTierVi = alMemTierViRepository.save(alMemTierVi);
        return alMemTierViMapper.toDto(alMemTierVi);
    }

    /**
     * Update a alMemTierVi.
     *
     * @param alMemTierViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlMemTierViDTO update(AlMemTierViDTO alMemTierViDTO) {
        LOG.debug("Request to update AlMemTierVi : {}", alMemTierViDTO);
        AlMemTierVi alMemTierVi = alMemTierViMapper.toEntity(alMemTierViDTO);
        alMemTierVi = alMemTierViRepository.save(alMemTierVi);
        return alMemTierViMapper.toDto(alMemTierVi);
    }

    /**
     * Partially update a alMemTierVi.
     *
     * @param alMemTierViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlMemTierViDTO> partialUpdate(AlMemTierViDTO alMemTierViDTO) {
        LOG.debug("Request to partially update AlMemTierVi : {}", alMemTierViDTO);

        return alMemTierViRepository
            .findById(alMemTierViDTO.getId())
            .map(existingAlMemTierVi -> {
                alMemTierViMapper.partialUpdate(existingAlMemTierVi, alMemTierViDTO);

                return existingAlMemTierVi;
            })
            .map(alMemTierViRepository::save)
            .map(alMemTierViMapper::toDto);
    }

    /**
     * Get one alMemTierVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlMemTierViDTO> findOne(Long id) {
        LOG.debug("Request to get AlMemTierVi : {}", id);
        return alMemTierViRepository.findById(id).map(alMemTierViMapper::toDto);
    }

    /**
     * Delete the alMemTierVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlMemTierVi : {}", id);
        alMemTierViRepository.deleteById(id);
    }
}
