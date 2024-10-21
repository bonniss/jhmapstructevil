package ai.realworld.service;

import ai.realworld.domain.AlPowerShell;
import ai.realworld.repository.AlPowerShellRepository;
import ai.realworld.service.dto.AlPowerShellDTO;
import ai.realworld.service.mapper.AlPowerShellMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPowerShell}.
 */
@Service
@Transactional
public class AlPowerShellService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPowerShellService.class);

    private final AlPowerShellRepository alPowerShellRepository;

    private final AlPowerShellMapper alPowerShellMapper;

    public AlPowerShellService(AlPowerShellRepository alPowerShellRepository, AlPowerShellMapper alPowerShellMapper) {
        this.alPowerShellRepository = alPowerShellRepository;
        this.alPowerShellMapper = alPowerShellMapper;
    }

    /**
     * Save a alPowerShell.
     *
     * @param alPowerShellDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPowerShellDTO save(AlPowerShellDTO alPowerShellDTO) {
        LOG.debug("Request to save AlPowerShell : {}", alPowerShellDTO);
        AlPowerShell alPowerShell = alPowerShellMapper.toEntity(alPowerShellDTO);
        alPowerShell = alPowerShellRepository.save(alPowerShell);
        return alPowerShellMapper.toDto(alPowerShell);
    }

    /**
     * Update a alPowerShell.
     *
     * @param alPowerShellDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPowerShellDTO update(AlPowerShellDTO alPowerShellDTO) {
        LOG.debug("Request to update AlPowerShell : {}", alPowerShellDTO);
        AlPowerShell alPowerShell = alPowerShellMapper.toEntity(alPowerShellDTO);
        alPowerShell = alPowerShellRepository.save(alPowerShell);
        return alPowerShellMapper.toDto(alPowerShell);
    }

    /**
     * Partially update a alPowerShell.
     *
     * @param alPowerShellDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPowerShellDTO> partialUpdate(AlPowerShellDTO alPowerShellDTO) {
        LOG.debug("Request to partially update AlPowerShell : {}", alPowerShellDTO);

        return alPowerShellRepository
            .findById(alPowerShellDTO.getId())
            .map(existingAlPowerShell -> {
                alPowerShellMapper.partialUpdate(existingAlPowerShell, alPowerShellDTO);

                return existingAlPowerShell;
            })
            .map(alPowerShellRepository::save)
            .map(alPowerShellMapper::toDto);
    }

    /**
     * Get one alPowerShell by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPowerShellDTO> findOne(Long id) {
        LOG.debug("Request to get AlPowerShell : {}", id);
        return alPowerShellRepository.findById(id).map(alPowerShellMapper::toDto);
    }

    /**
     * Delete the alPowerShell by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPowerShell : {}", id);
        alPowerShellRepository.deleteById(id);
    }
}
