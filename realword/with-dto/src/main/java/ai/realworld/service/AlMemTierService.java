package ai.realworld.service;

import ai.realworld.domain.AlMemTier;
import ai.realworld.repository.AlMemTierRepository;
import ai.realworld.service.dto.AlMemTierDTO;
import ai.realworld.service.mapper.AlMemTierMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlMemTier}.
 */
@Service
@Transactional
public class AlMemTierService {

    private static final Logger LOG = LoggerFactory.getLogger(AlMemTierService.class);

    private final AlMemTierRepository alMemTierRepository;

    private final AlMemTierMapper alMemTierMapper;

    public AlMemTierService(AlMemTierRepository alMemTierRepository, AlMemTierMapper alMemTierMapper) {
        this.alMemTierRepository = alMemTierRepository;
        this.alMemTierMapper = alMemTierMapper;
    }

    /**
     * Save a alMemTier.
     *
     * @param alMemTierDTO the entity to save.
     * @return the persisted entity.
     */
    public AlMemTierDTO save(AlMemTierDTO alMemTierDTO) {
        LOG.debug("Request to save AlMemTier : {}", alMemTierDTO);
        AlMemTier alMemTier = alMemTierMapper.toEntity(alMemTierDTO);
        alMemTier = alMemTierRepository.save(alMemTier);
        return alMemTierMapper.toDto(alMemTier);
    }

    /**
     * Update a alMemTier.
     *
     * @param alMemTierDTO the entity to save.
     * @return the persisted entity.
     */
    public AlMemTierDTO update(AlMemTierDTO alMemTierDTO) {
        LOG.debug("Request to update AlMemTier : {}", alMemTierDTO);
        AlMemTier alMemTier = alMemTierMapper.toEntity(alMemTierDTO);
        alMemTier = alMemTierRepository.save(alMemTier);
        return alMemTierMapper.toDto(alMemTier);
    }

    /**
     * Partially update a alMemTier.
     *
     * @param alMemTierDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlMemTierDTO> partialUpdate(AlMemTierDTO alMemTierDTO) {
        LOG.debug("Request to partially update AlMemTier : {}", alMemTierDTO);

        return alMemTierRepository
            .findById(alMemTierDTO.getId())
            .map(existingAlMemTier -> {
                alMemTierMapper.partialUpdate(existingAlMemTier, alMemTierDTO);

                return existingAlMemTier;
            })
            .map(alMemTierRepository::save)
            .map(alMemTierMapper::toDto);
    }

    /**
     * Get one alMemTier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlMemTierDTO> findOne(Long id) {
        LOG.debug("Request to get AlMemTier : {}", id);
        return alMemTierRepository.findById(id).map(alMemTierMapper::toDto);
    }

    /**
     * Delete the alMemTier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlMemTier : {}", id);
        alMemTierRepository.deleteById(id);
    }
}
