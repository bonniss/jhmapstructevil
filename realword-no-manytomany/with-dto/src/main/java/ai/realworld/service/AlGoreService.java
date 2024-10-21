package ai.realworld.service;

import ai.realworld.domain.AlGore;
import ai.realworld.repository.AlGoreRepository;
import ai.realworld.service.dto.AlGoreDTO;
import ai.realworld.service.mapper.AlGoreMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlGore}.
 */
@Service
@Transactional
public class AlGoreService {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreService.class);

    private final AlGoreRepository alGoreRepository;

    private final AlGoreMapper alGoreMapper;

    public AlGoreService(AlGoreRepository alGoreRepository, AlGoreMapper alGoreMapper) {
        this.alGoreRepository = alGoreRepository;
        this.alGoreMapper = alGoreMapper;
    }

    /**
     * Save a alGore.
     *
     * @param alGoreDTO the entity to save.
     * @return the persisted entity.
     */
    public AlGoreDTO save(AlGoreDTO alGoreDTO) {
        LOG.debug("Request to save AlGore : {}", alGoreDTO);
        AlGore alGore = alGoreMapper.toEntity(alGoreDTO);
        alGore = alGoreRepository.save(alGore);
        return alGoreMapper.toDto(alGore);
    }

    /**
     * Update a alGore.
     *
     * @param alGoreDTO the entity to save.
     * @return the persisted entity.
     */
    public AlGoreDTO update(AlGoreDTO alGoreDTO) {
        LOG.debug("Request to update AlGore : {}", alGoreDTO);
        AlGore alGore = alGoreMapper.toEntity(alGoreDTO);
        alGore = alGoreRepository.save(alGore);
        return alGoreMapper.toDto(alGore);
    }

    /**
     * Partially update a alGore.
     *
     * @param alGoreDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlGoreDTO> partialUpdate(AlGoreDTO alGoreDTO) {
        LOG.debug("Request to partially update AlGore : {}", alGoreDTO);

        return alGoreRepository
            .findById(alGoreDTO.getId())
            .map(existingAlGore -> {
                alGoreMapper.partialUpdate(existingAlGore, alGoreDTO);

                return existingAlGore;
            })
            .map(alGoreRepository::save)
            .map(alGoreMapper::toDto);
    }

    /**
     * Get one alGore by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlGoreDTO> findOne(Long id) {
        LOG.debug("Request to get AlGore : {}", id);
        return alGoreRepository.findById(id).map(alGoreMapper::toDto);
    }

    /**
     * Delete the alGore by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlGore : {}", id);
        alGoreRepository.deleteById(id);
    }
}
