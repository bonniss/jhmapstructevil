package ai.realworld.service;

import ai.realworld.domain.AlVueVueUsage;
import ai.realworld.repository.AlVueVueUsageRepository;
import ai.realworld.service.dto.AlVueVueUsageDTO;
import ai.realworld.service.mapper.AlVueVueUsageMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlVueVueUsage}.
 */
@Service
@Transactional
public class AlVueVueUsageService {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueUsageService.class);

    private final AlVueVueUsageRepository alVueVueUsageRepository;

    private final AlVueVueUsageMapper alVueVueUsageMapper;

    public AlVueVueUsageService(AlVueVueUsageRepository alVueVueUsageRepository, AlVueVueUsageMapper alVueVueUsageMapper) {
        this.alVueVueUsageRepository = alVueVueUsageRepository;
        this.alVueVueUsageMapper = alVueVueUsageMapper;
    }

    /**
     * Save a alVueVueUsage.
     *
     * @param alVueVueUsageDTO the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueUsageDTO save(AlVueVueUsageDTO alVueVueUsageDTO) {
        LOG.debug("Request to save AlVueVueUsage : {}", alVueVueUsageDTO);
        AlVueVueUsage alVueVueUsage = alVueVueUsageMapper.toEntity(alVueVueUsageDTO);
        alVueVueUsage = alVueVueUsageRepository.save(alVueVueUsage);
        return alVueVueUsageMapper.toDto(alVueVueUsage);
    }

    /**
     * Update a alVueVueUsage.
     *
     * @param alVueVueUsageDTO the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueUsageDTO update(AlVueVueUsageDTO alVueVueUsageDTO) {
        LOG.debug("Request to update AlVueVueUsage : {}", alVueVueUsageDTO);
        AlVueVueUsage alVueVueUsage = alVueVueUsageMapper.toEntity(alVueVueUsageDTO);
        alVueVueUsage = alVueVueUsageRepository.save(alVueVueUsage);
        return alVueVueUsageMapper.toDto(alVueVueUsage);
    }

    /**
     * Partially update a alVueVueUsage.
     *
     * @param alVueVueUsageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlVueVueUsageDTO> partialUpdate(AlVueVueUsageDTO alVueVueUsageDTO) {
        LOG.debug("Request to partially update AlVueVueUsage : {}", alVueVueUsageDTO);

        return alVueVueUsageRepository
            .findById(alVueVueUsageDTO.getId())
            .map(existingAlVueVueUsage -> {
                alVueVueUsageMapper.partialUpdate(existingAlVueVueUsage, alVueVueUsageDTO);

                return existingAlVueVueUsage;
            })
            .map(alVueVueUsageRepository::save)
            .map(alVueVueUsageMapper::toDto);
    }

    /**
     * Get one alVueVueUsage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlVueVueUsageDTO> findOne(UUID id) {
        LOG.debug("Request to get AlVueVueUsage : {}", id);
        return alVueVueUsageRepository.findById(id).map(alVueVueUsageMapper::toDto);
    }

    /**
     * Delete the alVueVueUsage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlVueVueUsage : {}", id);
        alVueVueUsageRepository.deleteById(id);
    }
}
