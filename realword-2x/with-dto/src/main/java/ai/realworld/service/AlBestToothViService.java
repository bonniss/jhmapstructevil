package ai.realworld.service;

import ai.realworld.domain.AlBestToothVi;
import ai.realworld.repository.AlBestToothViRepository;
import ai.realworld.service.dto.AlBestToothViDTO;
import ai.realworld.service.mapper.AlBestToothViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlBestToothVi}.
 */
@Service
@Transactional
public class AlBestToothViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlBestToothViService.class);

    private final AlBestToothViRepository alBestToothViRepository;

    private final AlBestToothViMapper alBestToothViMapper;

    public AlBestToothViService(AlBestToothViRepository alBestToothViRepository, AlBestToothViMapper alBestToothViMapper) {
        this.alBestToothViRepository = alBestToothViRepository;
        this.alBestToothViMapper = alBestToothViMapper;
    }

    /**
     * Save a alBestToothVi.
     *
     * @param alBestToothViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlBestToothViDTO save(AlBestToothViDTO alBestToothViDTO) {
        LOG.debug("Request to save AlBestToothVi : {}", alBestToothViDTO);
        AlBestToothVi alBestToothVi = alBestToothViMapper.toEntity(alBestToothViDTO);
        alBestToothVi = alBestToothViRepository.save(alBestToothVi);
        return alBestToothViMapper.toDto(alBestToothVi);
    }

    /**
     * Update a alBestToothVi.
     *
     * @param alBestToothViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlBestToothViDTO update(AlBestToothViDTO alBestToothViDTO) {
        LOG.debug("Request to update AlBestToothVi : {}", alBestToothViDTO);
        AlBestToothVi alBestToothVi = alBestToothViMapper.toEntity(alBestToothViDTO);
        alBestToothVi = alBestToothViRepository.save(alBestToothVi);
        return alBestToothViMapper.toDto(alBestToothVi);
    }

    /**
     * Partially update a alBestToothVi.
     *
     * @param alBestToothViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlBestToothViDTO> partialUpdate(AlBestToothViDTO alBestToothViDTO) {
        LOG.debug("Request to partially update AlBestToothVi : {}", alBestToothViDTO);

        return alBestToothViRepository
            .findById(alBestToothViDTO.getId())
            .map(existingAlBestToothVi -> {
                alBestToothViMapper.partialUpdate(existingAlBestToothVi, alBestToothViDTO);

                return existingAlBestToothVi;
            })
            .map(alBestToothViRepository::save)
            .map(alBestToothViMapper::toDto);
    }

    /**
     * Get one alBestToothVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlBestToothViDTO> findOne(Long id) {
        LOG.debug("Request to get AlBestToothVi : {}", id);
        return alBestToothViRepository.findById(id).map(alBestToothViMapper::toDto);
    }

    /**
     * Delete the alBestToothVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlBestToothVi : {}", id);
        alBestToothViRepository.deleteById(id);
    }
}
