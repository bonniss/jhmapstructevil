package ai.realworld.service;

import ai.realworld.domain.AlAppleVi;
import ai.realworld.repository.AlAppleViRepository;
import ai.realworld.service.dto.AlAppleViDTO;
import ai.realworld.service.mapper.AlAppleViMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlAppleVi}.
 */
@Service
@Transactional
public class AlAppleViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlAppleViService.class);

    private final AlAppleViRepository alAppleViRepository;

    private final AlAppleViMapper alAppleViMapper;

    public AlAppleViService(AlAppleViRepository alAppleViRepository, AlAppleViMapper alAppleViMapper) {
        this.alAppleViRepository = alAppleViRepository;
        this.alAppleViMapper = alAppleViMapper;
    }

    /**
     * Save a alAppleVi.
     *
     * @param alAppleViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlAppleViDTO save(AlAppleViDTO alAppleViDTO) {
        LOG.debug("Request to save AlAppleVi : {}", alAppleViDTO);
        AlAppleVi alAppleVi = alAppleViMapper.toEntity(alAppleViDTO);
        alAppleVi = alAppleViRepository.save(alAppleVi);
        return alAppleViMapper.toDto(alAppleVi);
    }

    /**
     * Update a alAppleVi.
     *
     * @param alAppleViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlAppleViDTO update(AlAppleViDTO alAppleViDTO) {
        LOG.debug("Request to update AlAppleVi : {}", alAppleViDTO);
        AlAppleVi alAppleVi = alAppleViMapper.toEntity(alAppleViDTO);
        alAppleVi = alAppleViRepository.save(alAppleVi);
        return alAppleViMapper.toDto(alAppleVi);
    }

    /**
     * Partially update a alAppleVi.
     *
     * @param alAppleViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlAppleViDTO> partialUpdate(AlAppleViDTO alAppleViDTO) {
        LOG.debug("Request to partially update AlAppleVi : {}", alAppleViDTO);

        return alAppleViRepository
            .findById(alAppleViDTO.getId())
            .map(existingAlAppleVi -> {
                alAppleViMapper.partialUpdate(existingAlAppleVi, alAppleViDTO);

                return existingAlAppleVi;
            })
            .map(alAppleViRepository::save)
            .map(alAppleViMapper::toDto);
    }

    /**
     * Get one alAppleVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlAppleViDTO> findOne(UUID id) {
        LOG.debug("Request to get AlAppleVi : {}", id);
        return alAppleViRepository.findById(id).map(alAppleViMapper::toDto);
    }

    /**
     * Delete the alAppleVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlAppleVi : {}", id);
        alAppleViRepository.deleteById(id);
    }
}
