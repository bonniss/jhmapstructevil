package ai.realworld.service;

import ai.realworld.domain.AlLeandro;
import ai.realworld.repository.AlLeandroRepository;
import ai.realworld.service.dto.AlLeandroDTO;
import ai.realworld.service.mapper.AlLeandroMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlLeandro}.
 */
@Service
@Transactional
public class AlLeandroService {

    private static final Logger LOG = LoggerFactory.getLogger(AlLeandroService.class);

    private final AlLeandroRepository alLeandroRepository;

    private final AlLeandroMapper alLeandroMapper;

    public AlLeandroService(AlLeandroRepository alLeandroRepository, AlLeandroMapper alLeandroMapper) {
        this.alLeandroRepository = alLeandroRepository;
        this.alLeandroMapper = alLeandroMapper;
    }

    /**
     * Save a alLeandro.
     *
     * @param alLeandroDTO the entity to save.
     * @return the persisted entity.
     */
    public AlLeandroDTO save(AlLeandroDTO alLeandroDTO) {
        LOG.debug("Request to save AlLeandro : {}", alLeandroDTO);
        AlLeandro alLeandro = alLeandroMapper.toEntity(alLeandroDTO);
        alLeandro = alLeandroRepository.save(alLeandro);
        return alLeandroMapper.toDto(alLeandro);
    }

    /**
     * Update a alLeandro.
     *
     * @param alLeandroDTO the entity to save.
     * @return the persisted entity.
     */
    public AlLeandroDTO update(AlLeandroDTO alLeandroDTO) {
        LOG.debug("Request to update AlLeandro : {}", alLeandroDTO);
        AlLeandro alLeandro = alLeandroMapper.toEntity(alLeandroDTO);
        alLeandro = alLeandroRepository.save(alLeandro);
        return alLeandroMapper.toDto(alLeandro);
    }

    /**
     * Partially update a alLeandro.
     *
     * @param alLeandroDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlLeandroDTO> partialUpdate(AlLeandroDTO alLeandroDTO) {
        LOG.debug("Request to partially update AlLeandro : {}", alLeandroDTO);

        return alLeandroRepository
            .findById(alLeandroDTO.getId())
            .map(existingAlLeandro -> {
                alLeandroMapper.partialUpdate(existingAlLeandro, alLeandroDTO);

                return existingAlLeandro;
            })
            .map(alLeandroRepository::save)
            .map(alLeandroMapper::toDto);
    }

    /**
     * Get one alLeandro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlLeandroDTO> findOne(UUID id) {
        LOG.debug("Request to get AlLeandro : {}", id);
        return alLeandroRepository.findById(id).map(alLeandroMapper::toDto);
    }

    /**
     * Delete the alLeandro by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlLeandro : {}", id);
        alLeandroRepository.deleteById(id);
    }
}
