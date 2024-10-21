package ai.realworld.service;

import ai.realworld.domain.AlPyuDjibril;
import ai.realworld.repository.AlPyuDjibrilRepository;
import ai.realworld.service.dto.AlPyuDjibrilDTO;
import ai.realworld.service.mapper.AlPyuDjibrilMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPyuDjibril}.
 */
@Service
@Transactional
public class AlPyuDjibrilService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuDjibrilService.class);

    private final AlPyuDjibrilRepository alPyuDjibrilRepository;

    private final AlPyuDjibrilMapper alPyuDjibrilMapper;

    public AlPyuDjibrilService(AlPyuDjibrilRepository alPyuDjibrilRepository, AlPyuDjibrilMapper alPyuDjibrilMapper) {
        this.alPyuDjibrilRepository = alPyuDjibrilRepository;
        this.alPyuDjibrilMapper = alPyuDjibrilMapper;
    }

    /**
     * Save a alPyuDjibril.
     *
     * @param alPyuDjibrilDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPyuDjibrilDTO save(AlPyuDjibrilDTO alPyuDjibrilDTO) {
        LOG.debug("Request to save AlPyuDjibril : {}", alPyuDjibrilDTO);
        AlPyuDjibril alPyuDjibril = alPyuDjibrilMapper.toEntity(alPyuDjibrilDTO);
        alPyuDjibril = alPyuDjibrilRepository.save(alPyuDjibril);
        return alPyuDjibrilMapper.toDto(alPyuDjibril);
    }

    /**
     * Update a alPyuDjibril.
     *
     * @param alPyuDjibrilDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPyuDjibrilDTO update(AlPyuDjibrilDTO alPyuDjibrilDTO) {
        LOG.debug("Request to update AlPyuDjibril : {}", alPyuDjibrilDTO);
        AlPyuDjibril alPyuDjibril = alPyuDjibrilMapper.toEntity(alPyuDjibrilDTO);
        alPyuDjibril = alPyuDjibrilRepository.save(alPyuDjibril);
        return alPyuDjibrilMapper.toDto(alPyuDjibril);
    }

    /**
     * Partially update a alPyuDjibril.
     *
     * @param alPyuDjibrilDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPyuDjibrilDTO> partialUpdate(AlPyuDjibrilDTO alPyuDjibrilDTO) {
        LOG.debug("Request to partially update AlPyuDjibril : {}", alPyuDjibrilDTO);

        return alPyuDjibrilRepository
            .findById(alPyuDjibrilDTO.getId())
            .map(existingAlPyuDjibril -> {
                alPyuDjibrilMapper.partialUpdate(existingAlPyuDjibril, alPyuDjibrilDTO);

                return existingAlPyuDjibril;
            })
            .map(alPyuDjibrilRepository::save)
            .map(alPyuDjibrilMapper::toDto);
    }

    /**
     * Get one alPyuDjibril by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPyuDjibrilDTO> findOne(Long id) {
        LOG.debug("Request to get AlPyuDjibril : {}", id);
        return alPyuDjibrilRepository.findById(id).map(alPyuDjibrilMapper::toDto);
    }

    /**
     * Delete the alPyuDjibril by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPyuDjibril : {}", id);
        alPyuDjibrilRepository.deleteById(id);
    }
}
