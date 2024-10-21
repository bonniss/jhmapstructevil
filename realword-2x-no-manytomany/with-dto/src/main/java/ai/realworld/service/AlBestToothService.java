package ai.realworld.service;

import ai.realworld.domain.AlBestTooth;
import ai.realworld.repository.AlBestToothRepository;
import ai.realworld.service.dto.AlBestToothDTO;
import ai.realworld.service.mapper.AlBestToothMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlBestTooth}.
 */
@Service
@Transactional
public class AlBestToothService {

    private static final Logger LOG = LoggerFactory.getLogger(AlBestToothService.class);

    private final AlBestToothRepository alBestToothRepository;

    private final AlBestToothMapper alBestToothMapper;

    public AlBestToothService(AlBestToothRepository alBestToothRepository, AlBestToothMapper alBestToothMapper) {
        this.alBestToothRepository = alBestToothRepository;
        this.alBestToothMapper = alBestToothMapper;
    }

    /**
     * Save a alBestTooth.
     *
     * @param alBestToothDTO the entity to save.
     * @return the persisted entity.
     */
    public AlBestToothDTO save(AlBestToothDTO alBestToothDTO) {
        LOG.debug("Request to save AlBestTooth : {}", alBestToothDTO);
        AlBestTooth alBestTooth = alBestToothMapper.toEntity(alBestToothDTO);
        alBestTooth = alBestToothRepository.save(alBestTooth);
        return alBestToothMapper.toDto(alBestTooth);
    }

    /**
     * Update a alBestTooth.
     *
     * @param alBestToothDTO the entity to save.
     * @return the persisted entity.
     */
    public AlBestToothDTO update(AlBestToothDTO alBestToothDTO) {
        LOG.debug("Request to update AlBestTooth : {}", alBestToothDTO);
        AlBestTooth alBestTooth = alBestToothMapper.toEntity(alBestToothDTO);
        alBestTooth = alBestToothRepository.save(alBestTooth);
        return alBestToothMapper.toDto(alBestTooth);
    }

    /**
     * Partially update a alBestTooth.
     *
     * @param alBestToothDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlBestToothDTO> partialUpdate(AlBestToothDTO alBestToothDTO) {
        LOG.debug("Request to partially update AlBestTooth : {}", alBestToothDTO);

        return alBestToothRepository
            .findById(alBestToothDTO.getId())
            .map(existingAlBestTooth -> {
                alBestToothMapper.partialUpdate(existingAlBestTooth, alBestToothDTO);

                return existingAlBestTooth;
            })
            .map(alBestToothRepository::save)
            .map(alBestToothMapper::toDto);
    }

    /**
     * Get one alBestTooth by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlBestToothDTO> findOne(Long id) {
        LOG.debug("Request to get AlBestTooth : {}", id);
        return alBestToothRepository.findById(id).map(alBestToothMapper::toDto);
    }

    /**
     * Delete the alBestTooth by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlBestTooth : {}", id);
        alBestToothRepository.deleteById(id);
    }
}
