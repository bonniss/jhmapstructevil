package ai.realworld.service;

import ai.realworld.domain.AlVueVueViUsage;
import ai.realworld.repository.AlVueVueViUsageRepository;
import ai.realworld.service.dto.AlVueVueViUsageDTO;
import ai.realworld.service.mapper.AlVueVueViUsageMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlVueVueViUsage}.
 */
@Service
@Transactional
public class AlVueVueViUsageService {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueViUsageService.class);

    private final AlVueVueViUsageRepository alVueVueViUsageRepository;

    private final AlVueVueViUsageMapper alVueVueViUsageMapper;

    public AlVueVueViUsageService(AlVueVueViUsageRepository alVueVueViUsageRepository, AlVueVueViUsageMapper alVueVueViUsageMapper) {
        this.alVueVueViUsageRepository = alVueVueViUsageRepository;
        this.alVueVueViUsageMapper = alVueVueViUsageMapper;
    }

    /**
     * Save a alVueVueViUsage.
     *
     * @param alVueVueViUsageDTO the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueViUsageDTO save(AlVueVueViUsageDTO alVueVueViUsageDTO) {
        LOG.debug("Request to save AlVueVueViUsage : {}", alVueVueViUsageDTO);
        AlVueVueViUsage alVueVueViUsage = alVueVueViUsageMapper.toEntity(alVueVueViUsageDTO);
        alVueVueViUsage = alVueVueViUsageRepository.save(alVueVueViUsage);
        return alVueVueViUsageMapper.toDto(alVueVueViUsage);
    }

    /**
     * Update a alVueVueViUsage.
     *
     * @param alVueVueViUsageDTO the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueViUsageDTO update(AlVueVueViUsageDTO alVueVueViUsageDTO) {
        LOG.debug("Request to update AlVueVueViUsage : {}", alVueVueViUsageDTO);
        AlVueVueViUsage alVueVueViUsage = alVueVueViUsageMapper.toEntity(alVueVueViUsageDTO);
        alVueVueViUsage = alVueVueViUsageRepository.save(alVueVueViUsage);
        return alVueVueViUsageMapper.toDto(alVueVueViUsage);
    }

    /**
     * Partially update a alVueVueViUsage.
     *
     * @param alVueVueViUsageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlVueVueViUsageDTO> partialUpdate(AlVueVueViUsageDTO alVueVueViUsageDTO) {
        LOG.debug("Request to partially update AlVueVueViUsage : {}", alVueVueViUsageDTO);

        return alVueVueViUsageRepository
            .findById(alVueVueViUsageDTO.getId())
            .map(existingAlVueVueViUsage -> {
                alVueVueViUsageMapper.partialUpdate(existingAlVueVueViUsage, alVueVueViUsageDTO);

                return existingAlVueVueViUsage;
            })
            .map(alVueVueViUsageRepository::save)
            .map(alVueVueViUsageMapper::toDto);
    }

    /**
     * Get one alVueVueViUsage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlVueVueViUsageDTO> findOne(UUID id) {
        LOG.debug("Request to get AlVueVueViUsage : {}", id);
        return alVueVueViUsageRepository.findById(id).map(alVueVueViUsageMapper::toDto);
    }

    /**
     * Delete the alVueVueViUsage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlVueVueViUsage : {}", id);
        alVueVueViUsageRepository.deleteById(id);
    }
}
