package ai.realworld.service;

import ai.realworld.domain.AlPowerShellVi;
import ai.realworld.repository.AlPowerShellViRepository;
import ai.realworld.service.dto.AlPowerShellViDTO;
import ai.realworld.service.mapper.AlPowerShellViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPowerShellVi}.
 */
@Service
@Transactional
public class AlPowerShellViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPowerShellViService.class);

    private final AlPowerShellViRepository alPowerShellViRepository;

    private final AlPowerShellViMapper alPowerShellViMapper;

    public AlPowerShellViService(AlPowerShellViRepository alPowerShellViRepository, AlPowerShellViMapper alPowerShellViMapper) {
        this.alPowerShellViRepository = alPowerShellViRepository;
        this.alPowerShellViMapper = alPowerShellViMapper;
    }

    /**
     * Save a alPowerShellVi.
     *
     * @param alPowerShellViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPowerShellViDTO save(AlPowerShellViDTO alPowerShellViDTO) {
        LOG.debug("Request to save AlPowerShellVi : {}", alPowerShellViDTO);
        AlPowerShellVi alPowerShellVi = alPowerShellViMapper.toEntity(alPowerShellViDTO);
        alPowerShellVi = alPowerShellViRepository.save(alPowerShellVi);
        return alPowerShellViMapper.toDto(alPowerShellVi);
    }

    /**
     * Update a alPowerShellVi.
     *
     * @param alPowerShellViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPowerShellViDTO update(AlPowerShellViDTO alPowerShellViDTO) {
        LOG.debug("Request to update AlPowerShellVi : {}", alPowerShellViDTO);
        AlPowerShellVi alPowerShellVi = alPowerShellViMapper.toEntity(alPowerShellViDTO);
        alPowerShellVi = alPowerShellViRepository.save(alPowerShellVi);
        return alPowerShellViMapper.toDto(alPowerShellVi);
    }

    /**
     * Partially update a alPowerShellVi.
     *
     * @param alPowerShellViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPowerShellViDTO> partialUpdate(AlPowerShellViDTO alPowerShellViDTO) {
        LOG.debug("Request to partially update AlPowerShellVi : {}", alPowerShellViDTO);

        return alPowerShellViRepository
            .findById(alPowerShellViDTO.getId())
            .map(existingAlPowerShellVi -> {
                alPowerShellViMapper.partialUpdate(existingAlPowerShellVi, alPowerShellViDTO);

                return existingAlPowerShellVi;
            })
            .map(alPowerShellViRepository::save)
            .map(alPowerShellViMapper::toDto);
    }

    /**
     * Get one alPowerShellVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPowerShellViDTO> findOne(Long id) {
        LOG.debug("Request to get AlPowerShellVi : {}", id);
        return alPowerShellViRepository.findById(id).map(alPowerShellViMapper::toDto);
    }

    /**
     * Delete the alPowerShellVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPowerShellVi : {}", id);
        alPowerShellViRepository.deleteById(id);
    }
}
