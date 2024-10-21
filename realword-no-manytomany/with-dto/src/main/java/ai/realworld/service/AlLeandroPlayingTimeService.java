package ai.realworld.service;

import ai.realworld.domain.AlLeandroPlayingTime;
import ai.realworld.repository.AlLeandroPlayingTimeRepository;
import ai.realworld.service.dto.AlLeandroPlayingTimeDTO;
import ai.realworld.service.mapper.AlLeandroPlayingTimeMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlLeandroPlayingTime}.
 */
@Service
@Transactional
public class AlLeandroPlayingTimeService {

    private static final Logger LOG = LoggerFactory.getLogger(AlLeandroPlayingTimeService.class);

    private final AlLeandroPlayingTimeRepository alLeandroPlayingTimeRepository;

    private final AlLeandroPlayingTimeMapper alLeandroPlayingTimeMapper;

    public AlLeandroPlayingTimeService(
        AlLeandroPlayingTimeRepository alLeandroPlayingTimeRepository,
        AlLeandroPlayingTimeMapper alLeandroPlayingTimeMapper
    ) {
        this.alLeandroPlayingTimeRepository = alLeandroPlayingTimeRepository;
        this.alLeandroPlayingTimeMapper = alLeandroPlayingTimeMapper;
    }

    /**
     * Save a alLeandroPlayingTime.
     *
     * @param alLeandroPlayingTimeDTO the entity to save.
     * @return the persisted entity.
     */
    public AlLeandroPlayingTimeDTO save(AlLeandroPlayingTimeDTO alLeandroPlayingTimeDTO) {
        LOG.debug("Request to save AlLeandroPlayingTime : {}", alLeandroPlayingTimeDTO);
        AlLeandroPlayingTime alLeandroPlayingTime = alLeandroPlayingTimeMapper.toEntity(alLeandroPlayingTimeDTO);
        alLeandroPlayingTime = alLeandroPlayingTimeRepository.save(alLeandroPlayingTime);
        return alLeandroPlayingTimeMapper.toDto(alLeandroPlayingTime);
    }

    /**
     * Update a alLeandroPlayingTime.
     *
     * @param alLeandroPlayingTimeDTO the entity to save.
     * @return the persisted entity.
     */
    public AlLeandroPlayingTimeDTO update(AlLeandroPlayingTimeDTO alLeandroPlayingTimeDTO) {
        LOG.debug("Request to update AlLeandroPlayingTime : {}", alLeandroPlayingTimeDTO);
        AlLeandroPlayingTime alLeandroPlayingTime = alLeandroPlayingTimeMapper.toEntity(alLeandroPlayingTimeDTO);
        alLeandroPlayingTime = alLeandroPlayingTimeRepository.save(alLeandroPlayingTime);
        return alLeandroPlayingTimeMapper.toDto(alLeandroPlayingTime);
    }

    /**
     * Partially update a alLeandroPlayingTime.
     *
     * @param alLeandroPlayingTimeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlLeandroPlayingTimeDTO> partialUpdate(AlLeandroPlayingTimeDTO alLeandroPlayingTimeDTO) {
        LOG.debug("Request to partially update AlLeandroPlayingTime : {}", alLeandroPlayingTimeDTO);

        return alLeandroPlayingTimeRepository
            .findById(alLeandroPlayingTimeDTO.getId())
            .map(existingAlLeandroPlayingTime -> {
                alLeandroPlayingTimeMapper.partialUpdate(existingAlLeandroPlayingTime, alLeandroPlayingTimeDTO);

                return existingAlLeandroPlayingTime;
            })
            .map(alLeandroPlayingTimeRepository::save)
            .map(alLeandroPlayingTimeMapper::toDto);
    }

    /**
     * Get one alLeandroPlayingTime by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlLeandroPlayingTimeDTO> findOne(UUID id) {
        LOG.debug("Request to get AlLeandroPlayingTime : {}", id);
        return alLeandroPlayingTimeRepository.findById(id).map(alLeandroPlayingTimeMapper::toDto);
    }

    /**
     * Delete the alLeandroPlayingTime by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlLeandroPlayingTime : {}", id);
        alLeandroPlayingTimeRepository.deleteById(id);
    }
}
