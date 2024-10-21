package ai.realworld.service;

import ai.realworld.domain.AlLadyGaga;
import ai.realworld.repository.AlLadyGagaRepository;
import ai.realworld.service.dto.AlLadyGagaDTO;
import ai.realworld.service.mapper.AlLadyGagaMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlLadyGaga}.
 */
@Service
@Transactional
public class AlLadyGagaService {

    private static final Logger LOG = LoggerFactory.getLogger(AlLadyGagaService.class);

    private final AlLadyGagaRepository alLadyGagaRepository;

    private final AlLadyGagaMapper alLadyGagaMapper;

    public AlLadyGagaService(AlLadyGagaRepository alLadyGagaRepository, AlLadyGagaMapper alLadyGagaMapper) {
        this.alLadyGagaRepository = alLadyGagaRepository;
        this.alLadyGagaMapper = alLadyGagaMapper;
    }

    /**
     * Save a alLadyGaga.
     *
     * @param alLadyGagaDTO the entity to save.
     * @return the persisted entity.
     */
    public AlLadyGagaDTO save(AlLadyGagaDTO alLadyGagaDTO) {
        LOG.debug("Request to save AlLadyGaga : {}", alLadyGagaDTO);
        AlLadyGaga alLadyGaga = alLadyGagaMapper.toEntity(alLadyGagaDTO);
        alLadyGaga = alLadyGagaRepository.save(alLadyGaga);
        return alLadyGagaMapper.toDto(alLadyGaga);
    }

    /**
     * Update a alLadyGaga.
     *
     * @param alLadyGagaDTO the entity to save.
     * @return the persisted entity.
     */
    public AlLadyGagaDTO update(AlLadyGagaDTO alLadyGagaDTO) {
        LOG.debug("Request to update AlLadyGaga : {}", alLadyGagaDTO);
        AlLadyGaga alLadyGaga = alLadyGagaMapper.toEntity(alLadyGagaDTO);
        alLadyGaga = alLadyGagaRepository.save(alLadyGaga);
        return alLadyGagaMapper.toDto(alLadyGaga);
    }

    /**
     * Partially update a alLadyGaga.
     *
     * @param alLadyGagaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlLadyGagaDTO> partialUpdate(AlLadyGagaDTO alLadyGagaDTO) {
        LOG.debug("Request to partially update AlLadyGaga : {}", alLadyGagaDTO);

        return alLadyGagaRepository
            .findById(alLadyGagaDTO.getId())
            .map(existingAlLadyGaga -> {
                alLadyGagaMapper.partialUpdate(existingAlLadyGaga, alLadyGagaDTO);

                return existingAlLadyGaga;
            })
            .map(alLadyGagaRepository::save)
            .map(alLadyGagaMapper::toDto);
    }

    /**
     * Get one alLadyGaga by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlLadyGagaDTO> findOne(UUID id) {
        LOG.debug("Request to get AlLadyGaga : {}", id);
        return alLadyGagaRepository.findById(id).map(alLadyGagaMapper::toDto);
    }

    /**
     * Delete the alLadyGaga by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlLadyGaga : {}", id);
        alLadyGagaRepository.deleteById(id);
    }
}
