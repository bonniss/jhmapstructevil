package ai.realworld.service;

import ai.realworld.domain.AlGoreVi;
import ai.realworld.repository.AlGoreViRepository;
import ai.realworld.service.dto.AlGoreViDTO;
import ai.realworld.service.mapper.AlGoreViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlGoreVi}.
 */
@Service
@Transactional
public class AlGoreViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreViService.class);

    private final AlGoreViRepository alGoreViRepository;

    private final AlGoreViMapper alGoreViMapper;

    public AlGoreViService(AlGoreViRepository alGoreViRepository, AlGoreViMapper alGoreViMapper) {
        this.alGoreViRepository = alGoreViRepository;
        this.alGoreViMapper = alGoreViMapper;
    }

    /**
     * Save a alGoreVi.
     *
     * @param alGoreViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlGoreViDTO save(AlGoreViDTO alGoreViDTO) {
        LOG.debug("Request to save AlGoreVi : {}", alGoreViDTO);
        AlGoreVi alGoreVi = alGoreViMapper.toEntity(alGoreViDTO);
        alGoreVi = alGoreViRepository.save(alGoreVi);
        return alGoreViMapper.toDto(alGoreVi);
    }

    /**
     * Update a alGoreVi.
     *
     * @param alGoreViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlGoreViDTO update(AlGoreViDTO alGoreViDTO) {
        LOG.debug("Request to update AlGoreVi : {}", alGoreViDTO);
        AlGoreVi alGoreVi = alGoreViMapper.toEntity(alGoreViDTO);
        alGoreVi = alGoreViRepository.save(alGoreVi);
        return alGoreViMapper.toDto(alGoreVi);
    }

    /**
     * Partially update a alGoreVi.
     *
     * @param alGoreViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlGoreViDTO> partialUpdate(AlGoreViDTO alGoreViDTO) {
        LOG.debug("Request to partially update AlGoreVi : {}", alGoreViDTO);

        return alGoreViRepository
            .findById(alGoreViDTO.getId())
            .map(existingAlGoreVi -> {
                alGoreViMapper.partialUpdate(existingAlGoreVi, alGoreViDTO);

                return existingAlGoreVi;
            })
            .map(alGoreViRepository::save)
            .map(alGoreViMapper::toDto);
    }

    /**
     * Get one alGoreVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlGoreViDTO> findOne(Long id) {
        LOG.debug("Request to get AlGoreVi : {}", id);
        return alGoreViRepository.findById(id).map(alGoreViMapper::toDto);
    }

    /**
     * Delete the alGoreVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlGoreVi : {}", id);
        alGoreViRepository.deleteById(id);
    }
}
