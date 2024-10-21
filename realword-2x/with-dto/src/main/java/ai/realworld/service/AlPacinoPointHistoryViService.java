package ai.realworld.service;

import ai.realworld.domain.AlPacinoPointHistoryVi;
import ai.realworld.repository.AlPacinoPointHistoryViRepository;
import ai.realworld.service.dto.AlPacinoPointHistoryViDTO;
import ai.realworld.service.mapper.AlPacinoPointHistoryViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPacinoPointHistoryVi}.
 */
@Service
@Transactional
public class AlPacinoPointHistoryViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoPointHistoryViService.class);

    private final AlPacinoPointHistoryViRepository alPacinoPointHistoryViRepository;

    private final AlPacinoPointHistoryViMapper alPacinoPointHistoryViMapper;

    public AlPacinoPointHistoryViService(
        AlPacinoPointHistoryViRepository alPacinoPointHistoryViRepository,
        AlPacinoPointHistoryViMapper alPacinoPointHistoryViMapper
    ) {
        this.alPacinoPointHistoryViRepository = alPacinoPointHistoryViRepository;
        this.alPacinoPointHistoryViMapper = alPacinoPointHistoryViMapper;
    }

    /**
     * Save a alPacinoPointHistoryVi.
     *
     * @param alPacinoPointHistoryViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoPointHistoryViDTO save(AlPacinoPointHistoryViDTO alPacinoPointHistoryViDTO) {
        LOG.debug("Request to save AlPacinoPointHistoryVi : {}", alPacinoPointHistoryViDTO);
        AlPacinoPointHistoryVi alPacinoPointHistoryVi = alPacinoPointHistoryViMapper.toEntity(alPacinoPointHistoryViDTO);
        alPacinoPointHistoryVi = alPacinoPointHistoryViRepository.save(alPacinoPointHistoryVi);
        return alPacinoPointHistoryViMapper.toDto(alPacinoPointHistoryVi);
    }

    /**
     * Update a alPacinoPointHistoryVi.
     *
     * @param alPacinoPointHistoryViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoPointHistoryViDTO update(AlPacinoPointHistoryViDTO alPacinoPointHistoryViDTO) {
        LOG.debug("Request to update AlPacinoPointHistoryVi : {}", alPacinoPointHistoryViDTO);
        AlPacinoPointHistoryVi alPacinoPointHistoryVi = alPacinoPointHistoryViMapper.toEntity(alPacinoPointHistoryViDTO);
        alPacinoPointHistoryVi = alPacinoPointHistoryViRepository.save(alPacinoPointHistoryVi);
        return alPacinoPointHistoryViMapper.toDto(alPacinoPointHistoryVi);
    }

    /**
     * Partially update a alPacinoPointHistoryVi.
     *
     * @param alPacinoPointHistoryViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPacinoPointHistoryViDTO> partialUpdate(AlPacinoPointHistoryViDTO alPacinoPointHistoryViDTO) {
        LOG.debug("Request to partially update AlPacinoPointHistoryVi : {}", alPacinoPointHistoryViDTO);

        return alPacinoPointHistoryViRepository
            .findById(alPacinoPointHistoryViDTO.getId())
            .map(existingAlPacinoPointHistoryVi -> {
                alPacinoPointHistoryViMapper.partialUpdate(existingAlPacinoPointHistoryVi, alPacinoPointHistoryViDTO);

                return existingAlPacinoPointHistoryVi;
            })
            .map(alPacinoPointHistoryViRepository::save)
            .map(alPacinoPointHistoryViMapper::toDto);
    }

    /**
     * Get one alPacinoPointHistoryVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPacinoPointHistoryViDTO> findOne(Long id) {
        LOG.debug("Request to get AlPacinoPointHistoryVi : {}", id);
        return alPacinoPointHistoryViRepository.findById(id).map(alPacinoPointHistoryViMapper::toDto);
    }

    /**
     * Delete the alPacinoPointHistoryVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPacinoPointHistoryVi : {}", id);
        alPacinoPointHistoryViRepository.deleteById(id);
    }
}
