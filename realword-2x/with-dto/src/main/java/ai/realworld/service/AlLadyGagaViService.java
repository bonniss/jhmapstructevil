package ai.realworld.service;

import ai.realworld.domain.AlLadyGagaVi;
import ai.realworld.repository.AlLadyGagaViRepository;
import ai.realworld.service.dto.AlLadyGagaViDTO;
import ai.realworld.service.mapper.AlLadyGagaViMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlLadyGagaVi}.
 */
@Service
@Transactional
public class AlLadyGagaViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlLadyGagaViService.class);

    private final AlLadyGagaViRepository alLadyGagaViRepository;

    private final AlLadyGagaViMapper alLadyGagaViMapper;

    public AlLadyGagaViService(AlLadyGagaViRepository alLadyGagaViRepository, AlLadyGagaViMapper alLadyGagaViMapper) {
        this.alLadyGagaViRepository = alLadyGagaViRepository;
        this.alLadyGagaViMapper = alLadyGagaViMapper;
    }

    /**
     * Save a alLadyGagaVi.
     *
     * @param alLadyGagaViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlLadyGagaViDTO save(AlLadyGagaViDTO alLadyGagaViDTO) {
        LOG.debug("Request to save AlLadyGagaVi : {}", alLadyGagaViDTO);
        AlLadyGagaVi alLadyGagaVi = alLadyGagaViMapper.toEntity(alLadyGagaViDTO);
        alLadyGagaVi = alLadyGagaViRepository.save(alLadyGagaVi);
        return alLadyGagaViMapper.toDto(alLadyGagaVi);
    }

    /**
     * Update a alLadyGagaVi.
     *
     * @param alLadyGagaViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlLadyGagaViDTO update(AlLadyGagaViDTO alLadyGagaViDTO) {
        LOG.debug("Request to update AlLadyGagaVi : {}", alLadyGagaViDTO);
        AlLadyGagaVi alLadyGagaVi = alLadyGagaViMapper.toEntity(alLadyGagaViDTO);
        alLadyGagaVi = alLadyGagaViRepository.save(alLadyGagaVi);
        return alLadyGagaViMapper.toDto(alLadyGagaVi);
    }

    /**
     * Partially update a alLadyGagaVi.
     *
     * @param alLadyGagaViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlLadyGagaViDTO> partialUpdate(AlLadyGagaViDTO alLadyGagaViDTO) {
        LOG.debug("Request to partially update AlLadyGagaVi : {}", alLadyGagaViDTO);

        return alLadyGagaViRepository
            .findById(alLadyGagaViDTO.getId())
            .map(existingAlLadyGagaVi -> {
                alLadyGagaViMapper.partialUpdate(existingAlLadyGagaVi, alLadyGagaViDTO);

                return existingAlLadyGagaVi;
            })
            .map(alLadyGagaViRepository::save)
            .map(alLadyGagaViMapper::toDto);
    }

    /**
     * Get one alLadyGagaVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlLadyGagaViDTO> findOne(UUID id) {
        LOG.debug("Request to get AlLadyGagaVi : {}", id);
        return alLadyGagaViRepository.findById(id).map(alLadyGagaViMapper::toDto);
    }

    /**
     * Delete the alLadyGagaVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlLadyGagaVi : {}", id);
        alLadyGagaViRepository.deleteById(id);
    }
}
