package ai.realworld.service;

import ai.realworld.domain.AlVueVueVi;
import ai.realworld.repository.AlVueVueViRepository;
import ai.realworld.service.dto.AlVueVueViDTO;
import ai.realworld.service.mapper.AlVueVueViMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlVueVueVi}.
 */
@Service
@Transactional
public class AlVueVueViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueViService.class);

    private final AlVueVueViRepository alVueVueViRepository;

    private final AlVueVueViMapper alVueVueViMapper;

    public AlVueVueViService(AlVueVueViRepository alVueVueViRepository, AlVueVueViMapper alVueVueViMapper) {
        this.alVueVueViRepository = alVueVueViRepository;
        this.alVueVueViMapper = alVueVueViMapper;
    }

    /**
     * Save a alVueVueVi.
     *
     * @param alVueVueViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueViDTO save(AlVueVueViDTO alVueVueViDTO) {
        LOG.debug("Request to save AlVueVueVi : {}", alVueVueViDTO);
        AlVueVueVi alVueVueVi = alVueVueViMapper.toEntity(alVueVueViDTO);
        alVueVueVi = alVueVueViRepository.save(alVueVueVi);
        return alVueVueViMapper.toDto(alVueVueVi);
    }

    /**
     * Update a alVueVueVi.
     *
     * @param alVueVueViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueViDTO update(AlVueVueViDTO alVueVueViDTO) {
        LOG.debug("Request to update AlVueVueVi : {}", alVueVueViDTO);
        AlVueVueVi alVueVueVi = alVueVueViMapper.toEntity(alVueVueViDTO);
        alVueVueVi = alVueVueViRepository.save(alVueVueVi);
        return alVueVueViMapper.toDto(alVueVueVi);
    }

    /**
     * Partially update a alVueVueVi.
     *
     * @param alVueVueViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlVueVueViDTO> partialUpdate(AlVueVueViDTO alVueVueViDTO) {
        LOG.debug("Request to partially update AlVueVueVi : {}", alVueVueViDTO);

        return alVueVueViRepository
            .findById(alVueVueViDTO.getId())
            .map(existingAlVueVueVi -> {
                alVueVueViMapper.partialUpdate(existingAlVueVueVi, alVueVueViDTO);

                return existingAlVueVueVi;
            })
            .map(alVueVueViRepository::save)
            .map(alVueVueViMapper::toDto);
    }

    /**
     * Get one alVueVueVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlVueVueViDTO> findOne(UUID id) {
        LOG.debug("Request to get AlVueVueVi : {}", id);
        return alVueVueViRepository.findById(id).map(alVueVueViMapper::toDto);
    }

    /**
     * Delete the alVueVueVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlVueVueVi : {}", id);
        alVueVueViRepository.deleteById(id);
    }
}
