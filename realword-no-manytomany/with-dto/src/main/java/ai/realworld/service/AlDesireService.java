package ai.realworld.service;

import ai.realworld.domain.AlDesire;
import ai.realworld.repository.AlDesireRepository;
import ai.realworld.service.dto.AlDesireDTO;
import ai.realworld.service.mapper.AlDesireMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlDesire}.
 */
@Service
@Transactional
public class AlDesireService {

    private static final Logger LOG = LoggerFactory.getLogger(AlDesireService.class);

    private final AlDesireRepository alDesireRepository;

    private final AlDesireMapper alDesireMapper;

    public AlDesireService(AlDesireRepository alDesireRepository, AlDesireMapper alDesireMapper) {
        this.alDesireRepository = alDesireRepository;
        this.alDesireMapper = alDesireMapper;
    }

    /**
     * Save a alDesire.
     *
     * @param alDesireDTO the entity to save.
     * @return the persisted entity.
     */
    public AlDesireDTO save(AlDesireDTO alDesireDTO) {
        LOG.debug("Request to save AlDesire : {}", alDesireDTO);
        AlDesire alDesire = alDesireMapper.toEntity(alDesireDTO);
        alDesire = alDesireRepository.save(alDesire);
        return alDesireMapper.toDto(alDesire);
    }

    /**
     * Update a alDesire.
     *
     * @param alDesireDTO the entity to save.
     * @return the persisted entity.
     */
    public AlDesireDTO update(AlDesireDTO alDesireDTO) {
        LOG.debug("Request to update AlDesire : {}", alDesireDTO);
        AlDesire alDesire = alDesireMapper.toEntity(alDesireDTO);
        alDesire = alDesireRepository.save(alDesire);
        return alDesireMapper.toDto(alDesire);
    }

    /**
     * Partially update a alDesire.
     *
     * @param alDesireDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlDesireDTO> partialUpdate(AlDesireDTO alDesireDTO) {
        LOG.debug("Request to partially update AlDesire : {}", alDesireDTO);

        return alDesireRepository
            .findById(alDesireDTO.getId())
            .map(existingAlDesire -> {
                alDesireMapper.partialUpdate(existingAlDesire, alDesireDTO);

                return existingAlDesire;
            })
            .map(alDesireRepository::save)
            .map(alDesireMapper::toDto);
    }

    /**
     * Get one alDesire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlDesireDTO> findOne(UUID id) {
        LOG.debug("Request to get AlDesire : {}", id);
        return alDesireRepository.findById(id).map(alDesireMapper::toDto);
    }

    /**
     * Delete the alDesire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlDesire : {}", id);
        alDesireRepository.deleteById(id);
    }
}
