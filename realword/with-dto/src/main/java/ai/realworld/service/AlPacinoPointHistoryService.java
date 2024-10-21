package ai.realworld.service;

import ai.realworld.domain.AlPacinoPointHistory;
import ai.realworld.repository.AlPacinoPointHistoryRepository;
import ai.realworld.service.dto.AlPacinoPointHistoryDTO;
import ai.realworld.service.mapper.AlPacinoPointHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPacinoPointHistory}.
 */
@Service
@Transactional
public class AlPacinoPointHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoPointHistoryService.class);

    private final AlPacinoPointHistoryRepository alPacinoPointHistoryRepository;

    private final AlPacinoPointHistoryMapper alPacinoPointHistoryMapper;

    public AlPacinoPointHistoryService(
        AlPacinoPointHistoryRepository alPacinoPointHistoryRepository,
        AlPacinoPointHistoryMapper alPacinoPointHistoryMapper
    ) {
        this.alPacinoPointHistoryRepository = alPacinoPointHistoryRepository;
        this.alPacinoPointHistoryMapper = alPacinoPointHistoryMapper;
    }

    /**
     * Save a alPacinoPointHistory.
     *
     * @param alPacinoPointHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoPointHistoryDTO save(AlPacinoPointHistoryDTO alPacinoPointHistoryDTO) {
        LOG.debug("Request to save AlPacinoPointHistory : {}", alPacinoPointHistoryDTO);
        AlPacinoPointHistory alPacinoPointHistory = alPacinoPointHistoryMapper.toEntity(alPacinoPointHistoryDTO);
        alPacinoPointHistory = alPacinoPointHistoryRepository.save(alPacinoPointHistory);
        return alPacinoPointHistoryMapper.toDto(alPacinoPointHistory);
    }

    /**
     * Update a alPacinoPointHistory.
     *
     * @param alPacinoPointHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoPointHistoryDTO update(AlPacinoPointHistoryDTO alPacinoPointHistoryDTO) {
        LOG.debug("Request to update AlPacinoPointHistory : {}", alPacinoPointHistoryDTO);
        AlPacinoPointHistory alPacinoPointHistory = alPacinoPointHistoryMapper.toEntity(alPacinoPointHistoryDTO);
        alPacinoPointHistory = alPacinoPointHistoryRepository.save(alPacinoPointHistory);
        return alPacinoPointHistoryMapper.toDto(alPacinoPointHistory);
    }

    /**
     * Partially update a alPacinoPointHistory.
     *
     * @param alPacinoPointHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPacinoPointHistoryDTO> partialUpdate(AlPacinoPointHistoryDTO alPacinoPointHistoryDTO) {
        LOG.debug("Request to partially update AlPacinoPointHistory : {}", alPacinoPointHistoryDTO);

        return alPacinoPointHistoryRepository
            .findById(alPacinoPointHistoryDTO.getId())
            .map(existingAlPacinoPointHistory -> {
                alPacinoPointHistoryMapper.partialUpdate(existingAlPacinoPointHistory, alPacinoPointHistoryDTO);

                return existingAlPacinoPointHistory;
            })
            .map(alPacinoPointHistoryRepository::save)
            .map(alPacinoPointHistoryMapper::toDto);
    }

    /**
     * Get one alPacinoPointHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPacinoPointHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get AlPacinoPointHistory : {}", id);
        return alPacinoPointHistoryRepository.findById(id).map(alPacinoPointHistoryMapper::toDto);
    }

    /**
     * Delete the alPacinoPointHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPacinoPointHistory : {}", id);
        alPacinoPointHistoryRepository.deleteById(id);
    }
}
