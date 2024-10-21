package ai.realworld.service;

import ai.realworld.domain.AlPyuJoker;
import ai.realworld.repository.AlPyuJokerRepository;
import ai.realworld.service.dto.AlPyuJokerDTO;
import ai.realworld.service.mapper.AlPyuJokerMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPyuJoker}.
 */
@Service
@Transactional
public class AlPyuJokerService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuJokerService.class);

    private final AlPyuJokerRepository alPyuJokerRepository;

    private final AlPyuJokerMapper alPyuJokerMapper;

    public AlPyuJokerService(AlPyuJokerRepository alPyuJokerRepository, AlPyuJokerMapper alPyuJokerMapper) {
        this.alPyuJokerRepository = alPyuJokerRepository;
        this.alPyuJokerMapper = alPyuJokerMapper;
    }

    /**
     * Save a alPyuJoker.
     *
     * @param alPyuJokerDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPyuJokerDTO save(AlPyuJokerDTO alPyuJokerDTO) {
        LOG.debug("Request to save AlPyuJoker : {}", alPyuJokerDTO);
        AlPyuJoker alPyuJoker = alPyuJokerMapper.toEntity(alPyuJokerDTO);
        alPyuJoker = alPyuJokerRepository.save(alPyuJoker);
        return alPyuJokerMapper.toDto(alPyuJoker);
    }

    /**
     * Update a alPyuJoker.
     *
     * @param alPyuJokerDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPyuJokerDTO update(AlPyuJokerDTO alPyuJokerDTO) {
        LOG.debug("Request to update AlPyuJoker : {}", alPyuJokerDTO);
        AlPyuJoker alPyuJoker = alPyuJokerMapper.toEntity(alPyuJokerDTO);
        alPyuJoker = alPyuJokerRepository.save(alPyuJoker);
        return alPyuJokerMapper.toDto(alPyuJoker);
    }

    /**
     * Partially update a alPyuJoker.
     *
     * @param alPyuJokerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPyuJokerDTO> partialUpdate(AlPyuJokerDTO alPyuJokerDTO) {
        LOG.debug("Request to partially update AlPyuJoker : {}", alPyuJokerDTO);

        return alPyuJokerRepository
            .findById(alPyuJokerDTO.getId())
            .map(existingAlPyuJoker -> {
                alPyuJokerMapper.partialUpdate(existingAlPyuJoker, alPyuJokerDTO);

                return existingAlPyuJoker;
            })
            .map(alPyuJokerRepository::save)
            .map(alPyuJokerMapper::toDto);
    }

    /**
     * Get one alPyuJoker by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPyuJokerDTO> findOne(UUID id) {
        LOG.debug("Request to get AlPyuJoker : {}", id);
        return alPyuJokerRepository.findById(id).map(alPyuJokerMapper::toDto);
    }

    /**
     * Delete the alPyuJoker by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlPyuJoker : {}", id);
        alPyuJokerRepository.deleteById(id);
    }
}
