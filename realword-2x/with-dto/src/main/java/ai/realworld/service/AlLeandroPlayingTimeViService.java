package ai.realworld.service;

import ai.realworld.domain.AlLeandroPlayingTimeVi;
import ai.realworld.repository.AlLeandroPlayingTimeViRepository;
import ai.realworld.service.dto.AlLeandroPlayingTimeViDTO;
import ai.realworld.service.mapper.AlLeandroPlayingTimeViMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlLeandroPlayingTimeVi}.
 */
@Service
@Transactional
public class AlLeandroPlayingTimeViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlLeandroPlayingTimeViService.class);

    private final AlLeandroPlayingTimeViRepository alLeandroPlayingTimeViRepository;

    private final AlLeandroPlayingTimeViMapper alLeandroPlayingTimeViMapper;

    public AlLeandroPlayingTimeViService(
        AlLeandroPlayingTimeViRepository alLeandroPlayingTimeViRepository,
        AlLeandroPlayingTimeViMapper alLeandroPlayingTimeViMapper
    ) {
        this.alLeandroPlayingTimeViRepository = alLeandroPlayingTimeViRepository;
        this.alLeandroPlayingTimeViMapper = alLeandroPlayingTimeViMapper;
    }

    /**
     * Save a alLeandroPlayingTimeVi.
     *
     * @param alLeandroPlayingTimeViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlLeandroPlayingTimeViDTO save(AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO) {
        LOG.debug("Request to save AlLeandroPlayingTimeVi : {}", alLeandroPlayingTimeViDTO);
        AlLeandroPlayingTimeVi alLeandroPlayingTimeVi = alLeandroPlayingTimeViMapper.toEntity(alLeandroPlayingTimeViDTO);
        alLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.save(alLeandroPlayingTimeVi);
        return alLeandroPlayingTimeViMapper.toDto(alLeandroPlayingTimeVi);
    }

    /**
     * Update a alLeandroPlayingTimeVi.
     *
     * @param alLeandroPlayingTimeViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlLeandroPlayingTimeViDTO update(AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO) {
        LOG.debug("Request to update AlLeandroPlayingTimeVi : {}", alLeandroPlayingTimeViDTO);
        AlLeandroPlayingTimeVi alLeandroPlayingTimeVi = alLeandroPlayingTimeViMapper.toEntity(alLeandroPlayingTimeViDTO);
        alLeandroPlayingTimeVi = alLeandroPlayingTimeViRepository.save(alLeandroPlayingTimeVi);
        return alLeandroPlayingTimeViMapper.toDto(alLeandroPlayingTimeVi);
    }

    /**
     * Partially update a alLeandroPlayingTimeVi.
     *
     * @param alLeandroPlayingTimeViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlLeandroPlayingTimeViDTO> partialUpdate(AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO) {
        LOG.debug("Request to partially update AlLeandroPlayingTimeVi : {}", alLeandroPlayingTimeViDTO);

        return alLeandroPlayingTimeViRepository
            .findById(alLeandroPlayingTimeViDTO.getId())
            .map(existingAlLeandroPlayingTimeVi -> {
                alLeandroPlayingTimeViMapper.partialUpdate(existingAlLeandroPlayingTimeVi, alLeandroPlayingTimeViDTO);

                return existingAlLeandroPlayingTimeVi;
            })
            .map(alLeandroPlayingTimeViRepository::save)
            .map(alLeandroPlayingTimeViMapper::toDto);
    }

    /**
     * Get one alLeandroPlayingTimeVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlLeandroPlayingTimeViDTO> findOne(UUID id) {
        LOG.debug("Request to get AlLeandroPlayingTimeVi : {}", id);
        return alLeandroPlayingTimeViRepository.findById(id).map(alLeandroPlayingTimeViMapper::toDto);
    }

    /**
     * Delete the alLeandroPlayingTimeVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlLeandroPlayingTimeVi : {}", id);
        alLeandroPlayingTimeViRepository.deleteById(id);
    }
}
