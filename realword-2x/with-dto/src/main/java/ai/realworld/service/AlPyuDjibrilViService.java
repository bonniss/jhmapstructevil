package ai.realworld.service;

import ai.realworld.domain.AlPyuDjibrilVi;
import ai.realworld.repository.AlPyuDjibrilViRepository;
import ai.realworld.service.dto.AlPyuDjibrilViDTO;
import ai.realworld.service.mapper.AlPyuDjibrilViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPyuDjibrilVi}.
 */
@Service
@Transactional
public class AlPyuDjibrilViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuDjibrilViService.class);

    private final AlPyuDjibrilViRepository alPyuDjibrilViRepository;

    private final AlPyuDjibrilViMapper alPyuDjibrilViMapper;

    public AlPyuDjibrilViService(AlPyuDjibrilViRepository alPyuDjibrilViRepository, AlPyuDjibrilViMapper alPyuDjibrilViMapper) {
        this.alPyuDjibrilViRepository = alPyuDjibrilViRepository;
        this.alPyuDjibrilViMapper = alPyuDjibrilViMapper;
    }

    /**
     * Save a alPyuDjibrilVi.
     *
     * @param alPyuDjibrilViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPyuDjibrilViDTO save(AlPyuDjibrilViDTO alPyuDjibrilViDTO) {
        LOG.debug("Request to save AlPyuDjibrilVi : {}", alPyuDjibrilViDTO);
        AlPyuDjibrilVi alPyuDjibrilVi = alPyuDjibrilViMapper.toEntity(alPyuDjibrilViDTO);
        alPyuDjibrilVi = alPyuDjibrilViRepository.save(alPyuDjibrilVi);
        return alPyuDjibrilViMapper.toDto(alPyuDjibrilVi);
    }

    /**
     * Update a alPyuDjibrilVi.
     *
     * @param alPyuDjibrilViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPyuDjibrilViDTO update(AlPyuDjibrilViDTO alPyuDjibrilViDTO) {
        LOG.debug("Request to update AlPyuDjibrilVi : {}", alPyuDjibrilViDTO);
        AlPyuDjibrilVi alPyuDjibrilVi = alPyuDjibrilViMapper.toEntity(alPyuDjibrilViDTO);
        alPyuDjibrilVi = alPyuDjibrilViRepository.save(alPyuDjibrilVi);
        return alPyuDjibrilViMapper.toDto(alPyuDjibrilVi);
    }

    /**
     * Partially update a alPyuDjibrilVi.
     *
     * @param alPyuDjibrilViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPyuDjibrilViDTO> partialUpdate(AlPyuDjibrilViDTO alPyuDjibrilViDTO) {
        LOG.debug("Request to partially update AlPyuDjibrilVi : {}", alPyuDjibrilViDTO);

        return alPyuDjibrilViRepository
            .findById(alPyuDjibrilViDTO.getId())
            .map(existingAlPyuDjibrilVi -> {
                alPyuDjibrilViMapper.partialUpdate(existingAlPyuDjibrilVi, alPyuDjibrilViDTO);

                return existingAlPyuDjibrilVi;
            })
            .map(alPyuDjibrilViRepository::save)
            .map(alPyuDjibrilViMapper::toDto);
    }

    /**
     * Get one alPyuDjibrilVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPyuDjibrilViDTO> findOne(Long id) {
        LOG.debug("Request to get AlPyuDjibrilVi : {}", id);
        return alPyuDjibrilViRepository.findById(id).map(alPyuDjibrilViMapper::toDto);
    }

    /**
     * Delete the alPyuDjibrilVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPyuDjibrilVi : {}", id);
        alPyuDjibrilViRepository.deleteById(id);
    }
}
