package ai.realworld.service;

import ai.realworld.domain.AlSherMaleVi;
import ai.realworld.repository.AlSherMaleViRepository;
import ai.realworld.service.dto.AlSherMaleViDTO;
import ai.realworld.service.mapper.AlSherMaleViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlSherMaleVi}.
 */
@Service
@Transactional
public class AlSherMaleViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlSherMaleViService.class);

    private final AlSherMaleViRepository alSherMaleViRepository;

    private final AlSherMaleViMapper alSherMaleViMapper;

    public AlSherMaleViService(AlSherMaleViRepository alSherMaleViRepository, AlSherMaleViMapper alSherMaleViMapper) {
        this.alSherMaleViRepository = alSherMaleViRepository;
        this.alSherMaleViMapper = alSherMaleViMapper;
    }

    /**
     * Save a alSherMaleVi.
     *
     * @param alSherMaleViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlSherMaleViDTO save(AlSherMaleViDTO alSherMaleViDTO) {
        LOG.debug("Request to save AlSherMaleVi : {}", alSherMaleViDTO);
        AlSherMaleVi alSherMaleVi = alSherMaleViMapper.toEntity(alSherMaleViDTO);
        alSherMaleVi = alSherMaleViRepository.save(alSherMaleVi);
        return alSherMaleViMapper.toDto(alSherMaleVi);
    }

    /**
     * Update a alSherMaleVi.
     *
     * @param alSherMaleViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlSherMaleViDTO update(AlSherMaleViDTO alSherMaleViDTO) {
        LOG.debug("Request to update AlSherMaleVi : {}", alSherMaleViDTO);
        AlSherMaleVi alSherMaleVi = alSherMaleViMapper.toEntity(alSherMaleViDTO);
        alSherMaleVi = alSherMaleViRepository.save(alSherMaleVi);
        return alSherMaleViMapper.toDto(alSherMaleVi);
    }

    /**
     * Partially update a alSherMaleVi.
     *
     * @param alSherMaleViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlSherMaleViDTO> partialUpdate(AlSherMaleViDTO alSherMaleViDTO) {
        LOG.debug("Request to partially update AlSherMaleVi : {}", alSherMaleViDTO);

        return alSherMaleViRepository
            .findById(alSherMaleViDTO.getId())
            .map(existingAlSherMaleVi -> {
                alSherMaleViMapper.partialUpdate(existingAlSherMaleVi, alSherMaleViDTO);

                return existingAlSherMaleVi;
            })
            .map(alSherMaleViRepository::save)
            .map(alSherMaleViMapper::toDto);
    }

    /**
     * Get one alSherMaleVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlSherMaleViDTO> findOne(Long id) {
        LOG.debug("Request to get AlSherMaleVi : {}", id);
        return alSherMaleViRepository.findById(id).map(alSherMaleViMapper::toDto);
    }

    /**
     * Delete the alSherMaleVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlSherMaleVi : {}", id);
        alSherMaleViRepository.deleteById(id);
    }
}
