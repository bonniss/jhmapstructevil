package ai.realworld.service;

import ai.realworld.domain.AlPyuThomasWayneVi;
import ai.realworld.repository.AlPyuThomasWayneViRepository;
import ai.realworld.service.dto.AlPyuThomasWayneViDTO;
import ai.realworld.service.mapper.AlPyuThomasWayneViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPyuThomasWayneVi}.
 */
@Service
@Transactional
public class AlPyuThomasWayneViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuThomasWayneViService.class);

    private final AlPyuThomasWayneViRepository alPyuThomasWayneViRepository;

    private final AlPyuThomasWayneViMapper alPyuThomasWayneViMapper;

    public AlPyuThomasWayneViService(
        AlPyuThomasWayneViRepository alPyuThomasWayneViRepository,
        AlPyuThomasWayneViMapper alPyuThomasWayneViMapper
    ) {
        this.alPyuThomasWayneViRepository = alPyuThomasWayneViRepository;
        this.alPyuThomasWayneViMapper = alPyuThomasWayneViMapper;
    }

    /**
     * Save a alPyuThomasWayneVi.
     *
     * @param alPyuThomasWayneViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPyuThomasWayneViDTO save(AlPyuThomasWayneViDTO alPyuThomasWayneViDTO) {
        LOG.debug("Request to save AlPyuThomasWayneVi : {}", alPyuThomasWayneViDTO);
        AlPyuThomasWayneVi alPyuThomasWayneVi = alPyuThomasWayneViMapper.toEntity(alPyuThomasWayneViDTO);
        alPyuThomasWayneVi = alPyuThomasWayneViRepository.save(alPyuThomasWayneVi);
        return alPyuThomasWayneViMapper.toDto(alPyuThomasWayneVi);
    }

    /**
     * Update a alPyuThomasWayneVi.
     *
     * @param alPyuThomasWayneViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPyuThomasWayneViDTO update(AlPyuThomasWayneViDTO alPyuThomasWayneViDTO) {
        LOG.debug("Request to update AlPyuThomasWayneVi : {}", alPyuThomasWayneViDTO);
        AlPyuThomasWayneVi alPyuThomasWayneVi = alPyuThomasWayneViMapper.toEntity(alPyuThomasWayneViDTO);
        alPyuThomasWayneVi = alPyuThomasWayneViRepository.save(alPyuThomasWayneVi);
        return alPyuThomasWayneViMapper.toDto(alPyuThomasWayneVi);
    }

    /**
     * Partially update a alPyuThomasWayneVi.
     *
     * @param alPyuThomasWayneViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPyuThomasWayneViDTO> partialUpdate(AlPyuThomasWayneViDTO alPyuThomasWayneViDTO) {
        LOG.debug("Request to partially update AlPyuThomasWayneVi : {}", alPyuThomasWayneViDTO);

        return alPyuThomasWayneViRepository
            .findById(alPyuThomasWayneViDTO.getId())
            .map(existingAlPyuThomasWayneVi -> {
                alPyuThomasWayneViMapper.partialUpdate(existingAlPyuThomasWayneVi, alPyuThomasWayneViDTO);

                return existingAlPyuThomasWayneVi;
            })
            .map(alPyuThomasWayneViRepository::save)
            .map(alPyuThomasWayneViMapper::toDto);
    }

    /**
     * Get one alPyuThomasWayneVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPyuThomasWayneViDTO> findOne(Long id) {
        LOG.debug("Request to get AlPyuThomasWayneVi : {}", id);
        return alPyuThomasWayneViRepository.findById(id).map(alPyuThomasWayneViMapper::toDto);
    }

    /**
     * Delete the alPyuThomasWayneVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPyuThomasWayneVi : {}", id);
        alPyuThomasWayneViRepository.deleteById(id);
    }
}
