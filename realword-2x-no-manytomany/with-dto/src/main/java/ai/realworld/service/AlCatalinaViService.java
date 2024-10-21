package ai.realworld.service;

import ai.realworld.domain.AlCatalinaVi;
import ai.realworld.repository.AlCatalinaViRepository;
import ai.realworld.service.dto.AlCatalinaViDTO;
import ai.realworld.service.mapper.AlCatalinaViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlCatalinaVi}.
 */
@Service
@Transactional
public class AlCatalinaViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlCatalinaViService.class);

    private final AlCatalinaViRepository alCatalinaViRepository;

    private final AlCatalinaViMapper alCatalinaViMapper;

    public AlCatalinaViService(AlCatalinaViRepository alCatalinaViRepository, AlCatalinaViMapper alCatalinaViMapper) {
        this.alCatalinaViRepository = alCatalinaViRepository;
        this.alCatalinaViMapper = alCatalinaViMapper;
    }

    /**
     * Save a alCatalinaVi.
     *
     * @param alCatalinaViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlCatalinaViDTO save(AlCatalinaViDTO alCatalinaViDTO) {
        LOG.debug("Request to save AlCatalinaVi : {}", alCatalinaViDTO);
        AlCatalinaVi alCatalinaVi = alCatalinaViMapper.toEntity(alCatalinaViDTO);
        alCatalinaVi = alCatalinaViRepository.save(alCatalinaVi);
        return alCatalinaViMapper.toDto(alCatalinaVi);
    }

    /**
     * Update a alCatalinaVi.
     *
     * @param alCatalinaViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlCatalinaViDTO update(AlCatalinaViDTO alCatalinaViDTO) {
        LOG.debug("Request to update AlCatalinaVi : {}", alCatalinaViDTO);
        AlCatalinaVi alCatalinaVi = alCatalinaViMapper.toEntity(alCatalinaViDTO);
        alCatalinaVi = alCatalinaViRepository.save(alCatalinaVi);
        return alCatalinaViMapper.toDto(alCatalinaVi);
    }

    /**
     * Partially update a alCatalinaVi.
     *
     * @param alCatalinaViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlCatalinaViDTO> partialUpdate(AlCatalinaViDTO alCatalinaViDTO) {
        LOG.debug("Request to partially update AlCatalinaVi : {}", alCatalinaViDTO);

        return alCatalinaViRepository
            .findById(alCatalinaViDTO.getId())
            .map(existingAlCatalinaVi -> {
                alCatalinaViMapper.partialUpdate(existingAlCatalinaVi, alCatalinaViDTO);

                return existingAlCatalinaVi;
            })
            .map(alCatalinaViRepository::save)
            .map(alCatalinaViMapper::toDto);
    }

    /**
     * Get one alCatalinaVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlCatalinaViDTO> findOne(Long id) {
        LOG.debug("Request to get AlCatalinaVi : {}", id);
        return alCatalinaViRepository.findById(id).map(alCatalinaViMapper::toDto);
    }

    /**
     * Delete the alCatalinaVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlCatalinaVi : {}", id);
        alCatalinaViRepository.deleteById(id);
    }
}
